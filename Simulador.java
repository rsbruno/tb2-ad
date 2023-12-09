import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;

public class Simulador {

    static Random random = new Random();

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

        Parametros params = new Parametros();
        PriorityQueue<Chamada> chamadas = new PriorityQueue<>();

        double tempo_chegada = geraTempoAleatorio(params.getMediaChegada());
        double tempo_saida = Double.MAX_VALUE;
        long fila = 0;
        long max_fila = 0;

        Litte e_n = new Litte();
        Litte e_w_chegada = new Litte();
        Litte e_w_saida = new Litte();

        double tempo_decorrido = 0.0;
        double tempo_chegada_pacote = Double.MAX_VALUE;
        double tempo_saida_pacote = Double.MAX_VALUE;
        double tempo_chegada_nova_chamada = geraTempoAleatorio(params.getMediaChegada());

        long chegadas = 0;
        long saidas = 0;

        while (tempo_decorrido < params.getTempoSimulacao()) {
            Chamada chamadaAtual = !chamadas.isEmpty() ? chamadas.peek() : null;

            tempo_chegada_pacote = !Objects.isNull(chamadaAtual) ? chamadaAtual.getProximoPacote() : Double.MAX_VALUE;

            // System.out.println("tempo_chegada_pacote: " + tempo_chegada_pacote);
            // System.out.println("tempo_saida_pacote: " + tempo_saida_pacote);
            // System.out.println("tempo_chegada_nova_chamada: " +
            //         tempo_chegada_nova_chamada);
            // System.out.println("=========================");

            tempo_decorrido = min(tempo_chegada_nova_chamada, min(tempo_chegada_pacote, tempo_saida_pacote));

            if (tempo_decorrido == tempo_chegada_nova_chamada) {
                double duracao_da_chamada = tempo_decorrido + params.getDuracaoLigacao();
                Chamada chamada = new Chamada(tempo_decorrido, duracao_da_chamada);
                chamadas.add(chamada);
                tempo_chegada_nova_chamada = tempo_decorrido + geraTempoAleatorio(params.getMediaChegada());
            } else if (tempo_decorrido == tempo_chegada_pacote) {
                chegadas++;

                if (fila == 0) {
                    tempo_saida_pacote = tempo_decorrido + params.getTempoEntrePacotes();
                }
                fila++;

                Chamada atualizaPacoteChamada = chamadas.poll();
                atualizaPacoteChamada.geraProximoPacote();
                // System.out.println(atualizaPacoteChamada.terminouAChamada());
                if (!atualizaPacoteChamada.terminouAChamada()) {
                    // System.out.println(
                    // atualizaPacoteChamada.getTempo_saida() + " : " +
                    // atualizaPacoteChamada.geraProximoPacote() +(
                    // atualizaPacoteChamada.getTempo_saida() <
                    // atualizaPacoteChamada.geraProximoPacote()));
                    chamadas.add(atualizaPacoteChamada);
                }

                max_fila = fila > max_fila ? fila : max_fila;
                // System.out.println("pegando eventos de chegada" + tempo_chegada_pacote);
                // chegou um pacote
            } else if (tempo_decorrido == tempo_saida_pacote) {
                fila--;
                saidas++;
                if (fila > 0l) {
                    tempo_saida_pacote = tempo_decorrido + params.getTempoEntrePacotes();
                    // soma_ocupacao += tempo_servico;
                } else {

                    tempo_saida_pacote = Double.MAX_VALUE;
                }
                // System.out.println("pegando eventos de saida" + tempo_saida_pacote);
            } else {
                System.out.println("nenhum evento");
                tempo_decorrido = params.getTempoSimulacao();
            }

            // if (tempo_decorrido == tempo_chegada) {
            // if (fila == 0) {
            // double tempo_servico = (-1.0 / params.getMediaServico()) *
            // Math.log(uniforme());
            // tempo_saida = tempo_decorrido + tempo_servico;
            // }
            // fila++;
            // max_fila = fila > max_fila ? fila : max_fila;
            // tempo_chegada = tempo_decorrido
            // + (-1.0 / params.getMediaChegada()) * Math.log(uniforme());
            // // E[N]
            // e_n.setSomaAreas(
            // e_n.getSomaAreas() + (tempo_decorrido - e_n.getTempoAnterior()) *
            // e_n.getNoEventos());
            // e_n.setNoEventos(e_n.getNoEventos() + 1);
            // e_n.setTempoAnterior(tempo_decorrido);
            // // E[W] - Chegada
            // e_w_chegada.setSomaAreas(
            // e_w_chegada.getSomaAreas()
            // + (tempo_decorrido - e_w_chegada.getTempoAnterior()) *
            // e_w_chegada.getNoEventos());
            // e_w_chegada.setNoEventos(e_w_chegada.getNoEventos() + 1);
            // e_w_chegada.setTempoAnterior(tempo_decorrido);
            // } else if (tempo_decorrido == tempo_saida) {
            // // saida
            // fila--;
            // if (fila != 0) {
            // double tempo_servico = (-1.0 / params.getMediaServico()) *
            // Math.log(uniforme());
            // tempo_saida = tempo_decorrido + tempo_servico;
            // } else {
            // tempo_saida = Double.MAX_VALUE;
            // }
            // e_n.setSomaAreas(e_n.getSomaAreas() + (tempo_decorrido -
            // e_n.getTempoAnterior()) * e_n.getNoEventos());
            // e_n.setNoEventos(e_n.getNoEventos() - 1);
            // e_n.setTempoAnterior(tempo_decorrido);

            // e_w_saida.setSomaAreas(
            // e_w_saida.getSomaAreas()
            // + (tempo_decorrido - e_w_saida.getTempoAnterior()) *
            // e_w_saida.getNoEventos());
            // e_w_saida.setNoEventos(e_w_saida.getNoEventos() + 1);
            // e_w_saida.setTempoAnterior(tempo_decorrido);
            // } else {
            // System.out.println("Evento invalido!");
            // return;
            // }
        }

        System.out.println("tamanho da arvore: " + chamadas.size());
        System.out.println("chegadas: " + chegadas);
        System.out.println("saidas: " + saidas);

        e_w_chegada.setSomaAreas(
                e_w_chegada.getSomaAreas()
                        + (tempo_decorrido - e_w_chegada.getTempoAnterior()) * e_w_chegada.getNoEventos());
        e_w_saida.setSomaAreas(
                e_w_saida.getSomaAreas() + (tempo_decorrido - e_w_saida.getTempoAnterior()) * e_w_saida.getNoEventos());

        double e_w_calculo = (e_w_chegada.getSomaAreas() - e_w_saida.getSomaAreas()) / e_w_chegada.getNoEventos();

        double e_n_calculo = e_n.getSomaAreas() / tempo_decorrido;

        double lambda = e_w_chegada.getNoEventos() / tempo_decorrido;

        System.out.println("tamanho maximo da fila: " + max_fila);
        System.out.printf("E[N]: %.6f\n", e_n_calculo);
        System.out.printf("E[W]: %.6f\n", e_w_calculo);
        System.out.printf("Erro de Little: %.20f\n", Math.abs(e_n_calculo - lambda * e_w_calculo));
    }
}