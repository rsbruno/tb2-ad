import java.util.Locale;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
        Locale.setDefault(Locale.US);
        random.setSeed(864000);
        System.out.println("tempo de simulação total: " + parametros.getTempoSimulacao());

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
                // System.out.println("duracao_da_chamada: " + d);
                Chamada chamada = new Chamada(tempo_decorrido, duracao_da_chamada);
                chamadas.add(chamada);
                chamadasSimultaneas = chamadas.size() > chamadasSimultaneas ? chamadas.size() : chamadasSimultaneas;
                tempo_chegada_nova_chamada = tempo_decorrido + geraTempoAleatorio(parametros.getMediaChegada());
            } else if (tempo_decorrido == tempo_chegada_pacote) {
                chegadas++;

                if (fila == 0) {
                    tempo_saida_pacote = tempo_decorrido + parametros.getTempoDeServico();
                    soma_ocupacao += parametros.getTempoDeServico();
                }
                fila++;

                Chamada chamada = chamadas.poll();
                chamada.geraProximoPacote();

                // System.out.println(atualizaPacoteChamada.terminouAChamada());
                if (!chamada.terminouAChamada()) {
                    chamadas.add(chamada);
                }

                max_fila = fila > max_fila ? fila : max_fila;
                e_n.atualizaSomaAreas((tempo_decorrido - e_n.getTempoAnterior()) * e_n.getNoEventos());
                e_n.setNoEventos(e_n.getNoEventos() + 1);
                e_n.setTempoAnterior(tempo_decorrido);
                // E[W] - Chegada
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

                double e_w_calculo = (e_w_chegada.getSomaAreas() - e_w_saida.getSomaAreas())
                        / e_w_chegada.getNoEventos();

                double e_n_calculo = e_n.getSomaAreas() / tempo_decorrido;

                double lambda = e_w_chegada.getNoEventos() / tempo_decorrido;
                try {
                    // Cria um objeto FileWriter com o caminho do arquivo
                    FileWriter fileWriter = new FileWriter("saida.csv", true);
                    // Cria um objeto BufferedWriter para escrever de forma mais eficiente
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    // Cria um objeto PrintWriter para escrever no arquivo
                    PrintWriter printWriter = new PrintWriter(bufferedWriter);
                    // Escreve no arquivo
                    printWriter.printf("%.6f,%.20f\n", tempo_coleta, Math.abs(e_n_calculo - lambda * e_w_calculo));
                    // Fecha os recursos
                    printWriter.close();
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tempo_coleta += 10;
            } else {
                System.out.println("nenhum evento");
                tempo_decorrido = parametros.getTempoSimulacao();
            }

        }

        e_w_chegada.atualizaSomaAreas((tempo_decorrido - e_w_chegada.getTempoAnterior()) * e_w_chegada.getNoEventos());

        e_w_saida.atualizaSomaAreas((tempo_decorrido - e_w_saida.getTempoAnterior()) * e_w_saida.getNoEventos());

        double e_w_calculo = (e_w_chegada.getSomaAreas() - e_w_saida.getSomaAreas()) / e_w_chegada.getNoEventos();

        double e_n_calculo = e_n.getSomaAreas() / tempo_decorrido;

        double lambda = e_w_chegada.getNoEventos() / tempo_decorrido;

        System.out.printf("tamanho final da arvore: %d\n", chamadas.size());
        System.out.printf("chamadas Simultaneas: %d\n", chamadasSimultaneas);
        System.out.printf("chegadas processadas: %d\n", chegadas);
        System.out.printf("saidas processadas: %d\n", saidas);
        System.out.printf("tamanho maximo da fila: %d\n", max_fila);
        System.out.printf("Ocupacao: %.6f\n", soma_ocupacao / tempo_decorrido);
        System.out.printf("E[N]: %.6f\n", e_n_calculo);
        System.out.printf("E[W]: %.6f\n", e_w_calculo);
        System.out.printf("Erro de Little: %.20f\n", Math.abs(e_n_calculo - lambda * e_w_calculo));
    }
}