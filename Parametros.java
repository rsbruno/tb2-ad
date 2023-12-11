public class Parametros {
    private double mediaChegada;
    private double mediaServico;
    private double tempoSimulacao;
    private double duracaoLigacao;
    private double tempoEntrePacotes;
    private double tempoDeServico;

    // Construtor
    public Parametros() {
        this.tempoSimulacao = 1800;
        this.duracaoLigacao = 1.0 / 120;

        this.mediaChegada = 1.0 / 0.01504;
        this.tempoEntrePacotes = 0.20;
        this.tempoDeServico = 0.01504;
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