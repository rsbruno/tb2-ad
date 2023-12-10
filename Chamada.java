public class Chamada implements Comparable<Chamada> {
    private Double proximoPacote;
    private Double tempo_saida;

    static Parametros parametros = new Parametros();

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
        if (proximoPacote > tempo_saida)
            return true;
        else
            return false;
    }

    public Double geraProximoPacote() {
        proximoPacote = proximoPacote + parametros.getTempoEntrePacotes();
        return this.proximoPacote;
    }

    @Override
    public int compareTo(Chamada chamada) {
        return Double.compare(this.proximoPacote, chamada.proximoPacote);
    }
}
