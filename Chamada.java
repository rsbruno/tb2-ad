public class Chamada implements Comparable<Chamada> {
    private Double proximoPacote;
    private Double tempo_saida;

    public Chamada(Double tempo_chegada, Double tempo_saida) {
        this.proximoPacote = tempo_chegada;
        this.tempo_saida = tempo_saida;
    }

    public Double getProximoPacote() {
        return this.proximoPacote;
    }

    public Double getTempo_saida() {
        return this.tempo_saida;
    }

    public Boolean terminouAChamada() {
        // System.out.println(this.tempo_saida + " : " + this.proximoPacote);
        if (proximoPacote > tempo_saida)
            return true;
        else
            return false;
    }

    public Double geraProximoPacote() {
        proximoPacote = proximoPacote + 0.02;
        return this.proximoPacote;
    }

    @Override
    public int compareTo(Chamada chamada) {
        return Double.compare(this.proximoPacote, chamada.proximoPacote);
    }
}
