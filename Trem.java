import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Classe que representa um trem no sistema
 * Gerencia estado, posição, passageiros e movimentação
 */
public class Trem {
    private int idTrem;
    private String origem;
    private String destino;
    private LocalTime horaSaida;
    private int passageiros;
    private No posicaoAtual;
    private int tempoParada; // minutos restantes de parada
    private boolean noDesvio;
    private boolean finalizado;
    private static Random random = new Random();
    
    public Trem(int idTrem, String origem, String destino, LocalTime horaSaida) {
        this.idTrem = idTrem;
        this.origem = origem;
        this.destino = destino;
        this.horaSaida = horaSaida;
        this.passageiros = random.nextInt(41) + 10; // 10 a 50 passageiros
        this.posicaoAtual = null;
        this.tempoParada = 0;
        this.noDesvio = false;
        this.finalizado = false;
    }
    
    // Getters e Setters
    public int getIdTrem() { 
        return idTrem; 
    }
    
    public String getOrigem() { 
        return origem; 
    }
    
    public String getDestino() { 
        return destino; 
    }
    
    public LocalTime getHoraSaida() { 
        return horaSaida; 
    }
    
    public int getPassageiros() { 
        return passageiros; 
    }
    
    public void setPassageiros(int passageiros) { 
        this.passageiros = passageiros; 
    }
    
    public No getPosicaoAtual() { 
        return posicaoAtual; 
    }
    
    public void setPosicaoAtual(No posicao) { 
        this.posicaoAtual = posicao; 
    }
    
    public int getTempoParada() { 
        return tempoParada; 
    }
    
    public void setTempoParada(int tempo) { 
        this.tempoParada = tempo; 
    }
    
    public boolean isNoDesvio() { 
        return noDesvio; 
    }
    
    public void setNoDesvio(boolean noDesvio) { 
        this.noDesvio = noDesvio; 
    }
    
    public boolean isFinalizado() { 
        return finalizado; 
    }
    
    public void setFinalizado(boolean finalizado) { 
        this.finalizado = finalizado; 
    }
    
    /**
     * Retorna informações detalhadas sobre o trem
     */
    public String getInfoDetalhada() {
        return String.format("Trem %d (%s→%s) - Partida: %s - Passageiros: %d",
                idTrem, origem, destino, 
                horaSaida.format(DateTimeFormatter.ofPattern("HH:mm")),
                passageiros);
    }
    
    /**
     * Verifica se o trem deve partir no horário atual
     */
    public boolean devePartir(LocalTime tempoAtual) {
        return posicaoAtual == null && tempoAtual.equals(horaSaida);
    }
    
    /**
     * Verifica se o trem pode se mover (não está parado e não está finalizado)
     */
    public boolean podeSevier() {
        return posicaoAtual != null && !finalizado && tempoParada == 0;
    }
    
    @Override
    public String toString() {
        return "T" + idTrem + "(" + origem + "→" + destino + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trem trem = (Trem) obj;
        return idTrem == trem.idTrem;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idTrem);
    }
}