public class Parametros {
    private double mediaChegada;
    private double mediaServico;
    private double tempoSimulacao;
    private double duracaoLigacao;
    private double tempoEntrePacotes;

    // Construtor
    public Parametros() {
        this.mediaChegada = 1.0 / 0.4;
        this.mediaServico = 1.0 / 0.6;
        this.tempoSimulacao = 1200;
        this.duracaoLigacao = 1200;
        this.tempoEntrePacotes = 0.02;
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

    public double getDuracaoLigacao() {
        return duracaoLigacao;
    }

    public double getTempoEntrePacotes() {
        return this.tempoEntrePacotes;
    }
}