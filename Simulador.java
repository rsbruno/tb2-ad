import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;

public class Simulador {

    static Random random = new Random();

    static Parametros parametros = new Parametros();

    static double geraTempoAleatorio(double maximo) {
        double rand = random.nextDouble(); // Gera um número aleatório no intervalo [0, 1)
        rand = 1.0 - rand;
        double valor = ((-1.0 / maximo) * Math.log(rand));
        return valor;
    }

    static double min(double d1, double d2) {
        if (d1 < d2)
            return d1;
        return d2;
    }

    public static void main(String[] args) {

        random.setSeed(864000);

        // System.out.println(1504/1000000000);

        PriorityQueue<Chamada> chamadas = new PriorityQueue<>();

        long fila = 0;
        long max_fila = 0;
        long chamadasSimultaneas = 0;
        double soma_ocupacao = 0.0;

        Litte e_n = new Litte();
        Litte e_w_chegada = new Litte();
        Litte e_w_saida = new Litte();

        double tempo_decorrido = 0.0;
        double tempo_chegada_pacote = Double.MAX_VALUE;
        double tempo_saida_pacote = Double.MAX_VALUE;
        double tempo_chegada_nova_chamada = geraTempoAleatorio(parametros.getMediaChegada());
        double tempo_coleta = 10.0;
        long maiorNoPacotes = 0;

        long chegadas = 0;
        long saidas = 0;

        while (tempo_decorrido < parametros.getTempoSimulacao()) {
            Chamada chamadaAtual = !chamadas.isEmpty() ? chamadas.peek() : null;

            tempo_chegada_pacote = !Objects.isNull(chamadaAtual) ? chamadaAtual.getProximoPacote() : Double.MAX_VALUE;

            tempo_decorrido = min(tempo_chegada_nova_chamada,
                    min(tempo_coleta, min(tempo_chegada_pacote, tempo_saida_pacote)));

            if (!Objects.isNull(chamadaAtual)) {
                maiorNoPacotes = chamadaAtual.getPacotesGerados() > maiorNoPacotes ? chamadaAtual.getPacotesGerados()
                        : maiorNoPacotes;
            }

            if (tempo_decorrido == tempo_chegada_nova_chamada) {
                double duracao_da_chamada = tempo_decorrido + geraTempoAleatorio(parametros.getDuracaoLigacao());
                Chamada chamada = new Chamada(tempo_decorrido, duracao_da_chamada);
                chamadas.add(chamada);
                chamadasSimultaneas = chamadas.size() > chamadasSimultaneas ? chamadas.size() : chamadasSimultaneas;
                tempo_chegada_nova_chamada = tempo_decorrido + geraTempoAleatorio(parametros.getMediaChegada());
            } else if (tempo_decorrido == tempo_chegada_pacote) {
                chegadas++;
                Chamada chamada = chamadas.poll();
                chamada.geraProximoPacote();
                if (!chamada.terminouAChamada()) {
                    chamadas.add(chamada);
                }
                if (fila == 0) {
                    tempo_saida_pacote = tempo_decorrido + parametros.getTempoDeServico();
                    soma_ocupacao += parametros.getTempoDeServico();
                }
                fila++;
                max_fila = fila > max_fila ? fila : max_fila;

                e_n.atualizaSomaAreas((tempo_decorrido - e_n.getTempoAnterior()) * e_n.getNoEventos());
                e_n.setNoEventos(e_n.getNoEventos() + 1);
                e_n.setTempoAnterior(tempo_decorrido);

                e_w_chegada.atualizaSomaAreas(
                        (tempo_decorrido - e_w_chegada.getTempoAnterior()) * e_w_chegada.getNoEventos());
                e_w_chegada.setNoEventos(e_w_chegada.getNoEventos() + 1);
                e_w_chegada.setTempoAnterior(tempo_decorrido);
            } else if (tempo_decorrido == tempo_saida_pacote) {
                fila--;
                saidas++;
                if (fila > 0l) {
                    tempo_saida_pacote = tempo_decorrido + parametros.getTempoDeServico();
                    soma_ocupacao += parametros.getTempoDeServico();
                } else {
                    tempo_saida_pacote = Double.MAX_VALUE;
                }
                e_n.atualizaSomaAreas((tempo_decorrido - e_n.getTempoAnterior()) * e_n.getNoEventos());
                e_n.setNoEventos(e_n.getNoEventos() - 1);
                e_n.setTempoAnterior(tempo_decorrido);

                e_w_saida.atualizaSomaAreas(
                        (tempo_decorrido - e_w_saida.getTempoAnterior()) * e_w_saida.getNoEventos());
                e_w_saida.setNoEventos(e_w_saida.getNoEventos() + 1);
                e_w_saida.setTempoAnterior(tempo_decorrido);
                // System.out.println("pegando eventos de saida" + tempo_saida_pacote);
            } else if (tempo_decorrido == tempo_coleta) {
                e_n.atualizaSomaAreas((tempo_decorrido - e_n.getTempoAnterior()) * e_n.getNoEventos());
                e_n.setTempoAnterior(tempo_decorrido);

                e_w_chegada.atualizaSomaAreas(
                        (tempo_decorrido - e_w_chegada.getTempoAnterior()) * e_w_chegada.getNoEventos());
                e_w_chegada.setTempoAnterior(tempo_decorrido);

                e_w_saida.atualizaSomaAreas(
                        (tempo_decorrido - e_w_saida.getTempoAnterior()) * e_w_saida.getNoEventos());
                e_w_saida.setTempoAnterior(tempo_decorrido);

                tempo_coleta += 20;
            } else {
                System.out.println("nenhum evento");
                tempo_decorrido = parametros.getTempoSimulacao();
            }

        }

        System.out.println("soma_ocupacao: " + soma_ocupacao);
        System.out.println("tamanho da arvore: " + chamadas.size());
        System.out.println("chamadasSimultaneas: " + chamadasSimultaneas);
        System.out.println("chegadas: " + chegadas);
        System.out.println("saidas: " + saidas);
        System.out.println("e_n.getSomaAreas(): " + e_n.getSomaAreas());

        e_w_chegada.atualizaSomaAreas((tempo_decorrido - e_w_chegada.getTempoAnterior()) * e_w_chegada.getNoEventos());

        e_w_saida.atualizaSomaAreas((tempo_decorrido - e_w_saida.getTempoAnterior()) * e_w_saida.getNoEventos());

        double e_w_calculo = (e_w_chegada.getSomaAreas() - e_w_saida.getSomaAreas()) / e_w_chegada.getNoEventos();

        double e_n_calculo = e_n.getSomaAreas() / tempo_decorrido;

        double lambda = e_w_chegada.getNoEventos() / tempo_decorrido;

        System.out.println("tamanho maximo da fila: " + max_fila);
        System.out.printf("Ocupacao: %.6f\n", soma_ocupacao / tempo_decorrido);
        System.out.printf("E[N]: %.6f\n", e_n_calculo);
        System.out.printf("E[W]: %.6f\n", e_w_calculo);
        System.out.printf("Erro de Little: %.20f\n", Math.abs(e_n_calculo - lambda * e_w_calculo));
    }
}