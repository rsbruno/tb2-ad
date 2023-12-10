public class Parametros {
    private double mediaChegada;
    private double mediaServico;
    private double tempoSimulacao;
    private double duracaoLigacao;
    private double tempoEntrePacotes;
    private double tempoAtendimento;

    // Construtor
    public Parametros() {
        this.mediaChegada = 1.0 / 0.01;
        this.mediaServico = 1.0 / 0.6;
        this.tempoSimulacao = 1800;
        this.duracaoLigacao = 120;
        this.tempoEntrePacotes = 0.02;
        this.tempoAtendimento = 0.000012;
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

    public double getTempoAtendimento() {
        return this.tempoAtendimento;
    }
}