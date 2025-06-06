/**
 * Classe que representa uma estação e suas estatísticas
 * Registra o total de passageiros que subiram e desceram
 */
public class Estacao {
    private int numeroEstacao;
    private int totalSubiram;
    private int totalDesceram;
    private int totalParadas; // número de vezes que trens pararam aqui
    
    public Estacao(int numeroEstacao) {
        this.numeroEstacao = numeroEstacao;
        this.totalSubiram = 0;
        this.totalDesceram = 0;
        this.totalParadas = 0;
    }
    
    /**
     * Adiciona movimentação de passageiros na estação
     * @param subiram número de passageiros que subiram
     * @param desceram número de passageiros que desceram
     */
    public void adicionarMovimentacao(int subiram, int desceram) {
        this.totalSubiram += subiram;
        this.totalDesceram += desceram;
        this.totalParadas++;
    }
    
    /**
     * Calcula o movimento líquido de passageiros (subiram - desceram)
     */
    public int getMovimentoLiquido() {
        return totalSubiram - totalDesceram;
    }
    
    /**
     * Calcula a média de passageiros por parada
     */
    public double getMediaPassageirasPoria() {
        if (totalParadas == 0) return 0.0;
        return (double)(totalSubiram + totalDesceram) / totalParadas;
    }
    
    // Getters
    public int getNumeroEstacao() { 
        return numeroEstacao; 
    }
    
    public int getTotalSubiram() { 
        return totalSubiram; 
    }
    
    public int getTotalDesceram() { 
        return totalDesceram; 
    }
    
    public int getTotalParadas() { 
        return totalParadas; 
    }
    
    public int getTotalMovimentacao() {
        return totalSubiram + totalDesceram;
    }
    
    @Override
    public String toString() {
        return String.format("Estação %d: %d subiram, %d desceram (%d paradas)", 
                           numeroEstacao, totalSubiram, totalDesceram, totalParadas);
    }
    
    /**
     * Retorna um relatório detalhado da estação
     */
    public String getRelatorioDetalhado() {
        return String.format(
            "=== ESTAÇÃO %d ===\n" +
            "Total de paradas: %d\n" +
            "Passageiros que subiram: %d\n" +
            "Passageiros que desceram: %d\n" +
            "Total de movimentação: %d\n" +
            "Movimento líquido: %+d\n" +
            "Média por parada: %.2f\n",
            numeroEstacao, totalParadas, totalSubiram, totalDesceram,
            getTotalMovimentacao(), getMovimentoLiquido(), getMediaPassageirasPoria()
        );
    }
}