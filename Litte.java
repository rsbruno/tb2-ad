public class Litte {
    private long noEventos;
    private double tempoAnterior;
    private double somaAreas;

    public Litte() {
        this.tempoAnterior = 0.0;
        this.somaAreas = 0.0;
        this.noEventos = 0;
    }

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

    public double atualizaSomaAreas(double incremento) {
        this.somaAreas = this.somaAreas + incremento;
        return this.somaAreas;
    }

}