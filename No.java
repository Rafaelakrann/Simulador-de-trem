// Classe que representa um nó na estrutura encadeada do trilho
class No {
    private String tipo; // "trilho", "estacao", "desvio", "ponto_A", "ponto_B"
    private int idNo;
    private No proximo;
    private No anterior;
    private Trem tremOcupando;
    private Trem desvioOcupado;
    
    public No(String tipo, int idNo) {
        this.tipo = tipo;
        this.idNo = idNo;
        this.proximo = null;
        this.anterior = null;
        this.tremOcupando = null;
        this.desvioOcupado = null;
    }
    
    // Getters e Setters
    public String getTipo() { return tipo; }
    public int getIdNo() { return idNo; }
    public No getProximo() { return proximo; }
    public void setProximo(No proximo) { this.proximo = proximo; }
    public No getAnterior() { return anterior; }
    public void setAnterior(No anterior) { this.anterior = anterior; }
    public Trem getTremOcupando() { return tremOcupando; }
    public void setTremOcupando(Trem trem) { this.tremOcupando = trem; }
    public Trem getDesvioOcupado() { return desvioOcupado; }
    public void setDesvioOcupado(Trem trem) { this.desvioOcupado = trem; }
    
    public boolean estaOcupado() {
        return tremOcupando != null;
    }
    
    public boolean desvioEstaOcupado() {
        return desvioOcupado != null;
    }
}