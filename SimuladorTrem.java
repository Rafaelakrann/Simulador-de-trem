import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
// Classe principal do simulador
public class SimuladorTrem {
    private int numeroEstacoes;
    private No pontoA;
    private No pontoB;
    private List<No> estacoes;
    private List<No> desvios;
    private List<Trem> trens;
    private List<Estacao> estatisticasEstacoes;
    private LocalTime tempoAtual;
    private int minutoSimulacao;
    private Random random;
    private Scanner scanner;
    
    public SimuladorTrem() {
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        this.trens = new ArrayList<>();
        this.estacoes = new ArrayList<>();
        this.desvios = new ArrayList<>();
        this.estatisticasEstacoes = new ArrayList<>();
        this.tempoAtual = LocalTime.of(8, 0); // Inicia às 8h
        this.minutoSimulacao = 0;
        this.numeroEstacoes = random.nextInt(21) + 10; // 10 a 30 estações
        
        System.out.println("=== SIMULADOR DE TREM ===");
        System.out.println("Número de estações: " + numeroEstacoes);
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
        
        criarTrilho();
        inicializarEstatisticas();
    }
    
    private void criarTrilho() {
        // Criar ponto A
        pontoA = new No("ponto_A", 0);
        No atual = pontoA;
        
        // Para cada estação, criar trilho + desvio antes + estação + desvio depois
        for (int i = 1; i <= numeroEstacoes; i++) {
            // Criar 19 nós de trilho (já que o desvio ocupa 1km dos 20km entre estações)
            for (int j = 1; j <= 19; j++) {
                No noTrilho = new No("trilho", atual.getIdNo() + 1);
                atual.setProximo(noTrilho);
                noTrilho.setAnterior(atual);
                atual = noTrilho;
            }
            
            // Criar desvio antes da estação
            No desvioAntes = new No("desvio", atual.getIdNo() + 1);
            atual.setProximo(desvioAntes);
            desvioAntes.setAnterior(atual);
            desvios.add(desvioAntes);
            atual = desvioAntes;
            
            // Criar estação
            No estacao = new No("estacao", atual.getIdNo() + 1);
            atual.setProximo(estacao);
            estacao.setAnterior(atual);
            estacoes.add(estacao);
            atual = estacao;
            
            // Criar desvio depois da estação
            No desvioDepois = new No("desvio", atual.getIdNo() + 1);
            atual.setProximo(desvioDepois);
            desvioDepois.setAnterior(atual);
            desvios.add(desvioDepois);
            atual = desvioDepois;
        }
        
        // Criar 20 nós de trilho até o ponto B
        for (int j = 1; j <= 20; j++) {
            No noTrilho = new No("trilho", atual.getIdNo() + 1);
            atual.setProximo(noTrilho);
            noTrilho.setAnterior(atual);
            atual = noTrilho;
        }
        
        // Criar ponto B
        pontoB = new No("ponto_B", atual.getIdNo() + 1);
        atual.setProximo(pontoB);
        pontoB.setAnterior(atual);
    }
    
    private void inicializarEstatisticas() {
        for (int i = 1; i <= numeroEstacoes; i++) {
            estatisticasEstacoes.add(new Estacao(i));
        }
    }
    
    private void criarTrens() {
        LocalTime horario = LocalTime.of(8, 0);
        LocalTime fimOperacao = LocalTime.of(17, 30);
        int idTrem = 1;
        
        while (!horario.isAfter(fimOperacao)) {
            // Criar trem A → B
            Trem tremAB = new Trem(idTrem++, "A", "B", horario);
            trens.add(tremAB);
            
            // Criar trem B → A
            Trem tremBA = new Trem(idTrem++, "B", "A", horario);
            trens.add(tremBA);
            
            horario = horario.plusMinutes(30);
        }
        
        System.out.println("Total de trens criados: " + trens.size());
    }
    
    private void simular() {
        criarTrens();
        boolean simulacaoAtiva = true;
        
        while (simulacaoAtiva) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("MINUTO " + minutoSimulacao + " - " + tempoAtual.format(DateTimeFormatter.ofPattern("HH:mm")));
            System.out.println("=".repeat(60));
            
            // Verificar se novos trens devem partir
            verificarPartidaTrens();
            
            // Mover todos os trens
            moverTrens();
            
            // Processar paradas em estações
            processarParadas();
            
            // Exibir estado atual
            exibirEstado();
            
            // Verificar se simulação deve continuar
            simulacaoAtiva = verificarContinuacao();
            
            if (simulacaoAtiva) {
                System.out.println("\nPressione ENTER para próximo minuto...");
                scanner.nextLine();
                
                minutoSimulacao++;
                tempoAtual = tempoAtual.plusMinutes(1);
            }
        }
        
        gerarRelatorioFinal();
    }
    
    private void verificarPartidaTrens() {
        for (Trem trem : trens) {
            if (trem.getPosicaoAtual() == null && 
                tempoAtual.equals(trem.getHoraSaida()) && 
                minutoSimulacao > 0) {
                
                if (trem.getOrigem().equals("A")) {
                    if (!pontoA.estaOcupado()) {
                        trem.setPosicaoAtual(pontoA);
                        pontoA.setTremOcupando(trem);
                        System.out.println("🚂 " + trem + " partiu do Ponto A com " + trem.getPassageiros() + " passageiros");
                    }
                } else {
                    if (!pontoB.estaOcupado()) {
                        trem.setPosicaoAtual(pontoB);
                        pontoB.setTremOcupando(trem);
                        System.out.println("🚂 " + trem + " partiu do Ponto B com " + trem.getPassageiros() + " passageiros");
                    }
                }
            }
        }
    }
    
    private void moverTrens() {
        for (Trem trem : trens) {
            if (trem.getPosicaoAtual() == null || trem.isFinalizado() || trem.getTempoParada() > 0) {
                continue;
            }
            
            if (trem.isNoDesvio()) {
                // Trem está no desvio, verificar se pode sair
                if (podeSeriraDoDesvio(trem)) {
                    sairDoDesvio(trem);
                }
            } else {
                // Trem está no trilho principal, tentar mover
                moverTremPrincipal(trem);
            }
        }
    }
    
    private void moverTremPrincipal(Trem trem) {
        No proximoNo = trem.getDestino().equals("B") ? 
            trem.getPosicaoAtual().getProximo() : 
            trem.getPosicaoAtual().getAnterior();
        
        if (proximoNo != null) {
            // Verificar se há conflito
            if (proximoNo.estaOcupado()) {
                // Tentar usar desvio
                if (podeUsarDesvio(trem)) {
                    entrarNoDesvio(trem);
                }
                // Se não pode usar desvio, fica parado
            } else {
                // Mover para próximo nó
                trem.getPosicaoAtual().setTremOcupando(null);
                proximoNo.setTremOcupando(trem);
                trem.setPosicaoAtual(proximoNo);
                
                // Verificar se chegou ao destino
                if ((proximoNo.getTipo().equals("ponto_B") && trem.getDestino().equals("B")) ||
                    (proximoNo.getTipo().equals("ponto_A") && trem.getDestino().equals("A"))) {
                    trem.setFinalizado(true);
                    proximoNo.setTremOcupando(null);
                    System.out.println("🏁 " + trem + " chegou ao destino!");
                }
            }
        }
    }
    
    private boolean podeUsarDesvio(Trem trem) {
        // Procurar desvios próximos
        for (No desvio : desvios) {
            if (desvio.getAnterior() != null && 
                desvio.getAnterior().equals(trem.getPosicaoAtual()) && 
                !desvio.desvioEstaOcupado()) {
                return true;
            }
        }
        return false;
    }
    
    private void entrarNoDesvio(Trem trem) {
        for (No desvio : desvios) {
            if (desvio.getAnterior() != null && 
                desvio.getAnterior().equals(trem.getPosicaoAtual()) && 
                !desvio.desvioEstaOcupado()) {
                
                trem.getPosicaoAtual().setTremOcupando(null);
                desvio.setDesvioOcupado(trem);
                trem.setPosicaoAtual(desvio);
                trem.setNoDesvio(true);
                System.out.println("🔄 " + trem + " entrou no desvio");
                break;
            }
        }
    }
    
    private boolean podeSeriraDoDesvio(Trem trem) {
        No proximoNo = trem.getDestino().equals("B") ? 
            trem.getPosicaoAtual().getProximo() : 
            trem.getPosicaoAtual().getAnterior();
        
        return proximoNo != null && !proximoNo.estaOcupado();
    }
    
    private void sairDoDesvio(Trem trem) {
        No proximoNo = trem.getDestino().equals("B") ? 
            trem.getPosicaoAtual().getProximo() : 
            trem.getPosicaoAtual().getAnterior();
        
        if (proximoNo != null) {
            trem.getPosicaoAtual().setDesvioOcupado(null);
            proximoNo.setTremOcupando(trem);
            trem.setPosicaoAtual(proximoNo);
            trem.setNoDesvio(false);
            System.out.println("🔄 " + trem + " saiu do desvio");
        }
    }
    
    private void processarParadas() {
        for (Trem trem : trens) {
            if (trem.getPosicaoAtual() != null && 
                trem.getPosicaoAtual().getTipo().equals("estacao") && 
                !trem.isFinalizado()) {
                
                if (trem.getTempoParada() == 0) {
                    // Primeira vez chegando na estação
                    iniciarParadaEstacao(trem);
                } else {
                    // Reduzir tempo de parada
                    trem.setTempoParada(trem.getTempoParada() - 1);
                    if (trem.getTempoParada() == 0) {
                        System.out.println("✅ " + trem + " terminou a parada na estação");
                    }
                }
            }
        }
    }
    
    private void iniciarParadaEstacao(Trem trem) {
        int numeroEstacao = estacoes.indexOf(trem.getPosicaoAtual()) + 1;
        
        // Gerar número de pessoas subindo e descendo (total par)
        int maxDescer = Math.min(10, trem.getPassageiros());
        int maxSubir = 10;
        
        int desceram = random.nextInt(maxDescer + 1);
        int subiram = random.nextInt(maxSubir + 1);
        
        // Garantir que total seja par
        int total = desceram + subiram;
        if (total % 2 != 0) {
            if (subiram > 0 && subiram < maxSubir) {
                subiram++;
            } else if (desceram > 0) {
                desceram--;
            }
        }
        
        // Atualizar passageiros do trem
        trem.setPassageiros(trem.getPassageiros() - desceram + subiram);
        
        // Registrar estatísticas
        estatisticasEstacoes.get(numeroEstacao - 1).adicionarMovimentacao(subiram, desceram);
        
        // Calcular tempo de parada (30 segundos por pessoa, convertido para minutos)
        int tempoParada = Math.max(1, (desceram + subiram) / 2); // Mínimo 1 minuto
        if (desceram == 0 && subiram == 0) {
            tempoParada = 1; // Parada mínima quando ninguém sobe/desce
        }
        
        trem.setTempoParada(tempoParada);
        
        System.out.println("🚏 " + trem + " parou na Estação " + numeroEstacao + 
                          " - Desceram: " + desceram + ", Subiram: " + subiram + 
                          " (Tempo parada: " + tempoParada + " min)");
    }
    
    private void exibirEstado() {
        System.out.println("\n📍 ESTADO ATUAL DOS TRENS:");
        
        for (Trem trem : trens) {
            if (trem.getPosicaoAtual() == null) {
                if (tempoAtual.isBefore(trem.getHoraSaida())) {
                    System.out.println("⏳ " + trem + " aguardando partida às " + 
                                     trem.getHoraSaida().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            } else if (!trem.isFinalizado()) {
                String status = "";
                if (trem.getTempoParada() > 0) {
                    status = " (parado por " + trem.getTempoParada() + " min)";
                } else if (trem.isNoDesvio()) {
                    status = " (no desvio)";
                }
                
                System.out.println("🚂 " + trem + " em " + trem.getPosicaoAtual().getTipo() + 
                                 " (ID: " + trem.getPosicaoAtual().getIdNo() + ")" + status + 
                                 " - " + trem.getPassageiros() + " passageiros");
            }
        }
    }
    
    private boolean verificarContinuacao() {
        // Verificar se ainda há trens ativos ou por partir
        for (Trem trem : trens) {
            if (!trem.isFinalizado() && 
                (trem.getPosicaoAtual() != null || tempoAtual.isBefore(trem.getHoraSaida()))) {
                return true;
            }
        }
        return false;
    }
    
    private void gerarRelatorioFinal() {
        try {
            FileWriter writer = new FileWriter("relatorio_simulacao.txt");
            writer.write("=== RELATÓRIO FINAL DA SIMULAÇÃO ===\n");
            writer.write("Data/Hora: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            writer.write("Número de estações: " + numeroEstacoes + "\n");
            writer.write("Total de trens: " + trens.size() + "\n\n");
            
            writer.write("ESTATÍSTICAS POR ESTAÇÃO:\n");
            writer.write("-".repeat(50) + "\n");
            
            int totalSubiram = 0;
            int totalDesceram = 0;
            
            for (Estacao estacao : estatisticasEstacoes) {
                writer.write(String.format("Estação %2d: %3d subiram, %3d desceram\n", 
                           estacao.getNumeroEstacao(), 
                           estacao.getTotalSubiram(), 
                           estacao.getTotalDesceram()));
                totalSubiram += estacao.getTotalSubiram();
                totalDesceram += estacao.getTotalDesceram();
            }
            
            writer.write("-".repeat(50) + "\n");
            writer.write(String.format("TOTAL GERAL: %d subiram, %d desceram\n", totalSubiram, totalDesceram));
            
            writer.close();
            System.out.println("\n📄 Relatório salvo em 'relatorio_simulacao.txt'");
            
        } catch (IOException e) {
            System.out.println("❌ Erro ao salvar relatório: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SimuladorTrem simulador = new SimuladorTrem();
        simulador.simular();
        System.out.println("\n🏁 Simulação finalizada!");
    }
}