public class Parametros {
    private double mediaChegada;
    private double mediaServico;
    private double tempoSimulacao;

    // Construtor
    public Parametros(double mediaChegada, double mediaServico, double tempoSimulacao) {
        this.mediaChegada = 1.0 / mediaChegada;
        this.mediaServico = 1.0 / mediaServico;
        this.tempoSimulacao = tempoSimulacao;
    }

    // Getters
    public double getMediaChegada() {
        return mediaChegada;
    }

    public double getMediaServico() {
        return mediaServico;
    }

    public double getTempoSimulacao() {
        return tempoSimulacao;
    }
}