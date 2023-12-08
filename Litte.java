public class Litte {
    private long noEventos;
    private double tempoAnterior;
    private double somaAreas;

    // Construtor
    public Litte() {
        iniciaLitte();
    }

    // Método para inicializar os atributos
    public void iniciaLitte() {
        setNoEventos(0);
        setTempoAnterior(0.0);
        setSomaAreas(0.0);
    }

    // Getters e Setters
    public long getNoEventos() {
        return noEventos;
    }

    public void setNoEventos(long noEventos) {
        this.noEventos = noEventos;
    }

    public double getTempoAnterior() {
        return tempoAnterior;
    }

    public void setTempoAnterior(double tempoAnterior) {
        this.tempoAnterior = tempoAnterior;
    }

    public double getSomaAreas() {
        return somaAreas;
    }

    public void setSomaAreas(double somaAreas) {
        this.somaAreas = somaAreas;
    }

}