public class Parametros {
    private double mediaChegada;
    private double mediaServico;
    private double tempoSimulacao;
    private double duracaoLigacao;
    private double tempoEntrePacotes;
    private double tempoDeServico;

    // Construtor
    public Parametros() {
        this.tempoSimulacao = 7200;
        this.duracaoLigacao = 1.0 / 120;

        this.mediaChegada = (1.0 / (0.6 / 0.0000752 / 60));
        this.tempoEntrePacotes = 0.02;
        this.tempoDeServico = (188 * 8 / 1000000000.00);
    }

    // Getters
    public double getMediaChegada() {
        return mediaChegada;
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

    public double getTempoDeServico() {
        return this.tempoDeServico;
    }
}