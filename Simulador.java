import java.util.Random;

public class Simulador {

    static Random random = new Random();

    static double uniforme() {
        double u = random.nextDouble(); // Gera um número aleatório no intervalo [0, 1)
        return 1.0 - u;
    }

    static double min(double d1, double d2) {
        if (d1 < d2)
            return d1;
        return d2;
    }

    public static void main(String[] args) {

        random.setSeed(864000);

        Parametros params = new Parametros(0.1, 0.06, 1200);
        double tempo_decorrido = 0.0;
        double tempo_chegada = (-1.0 / params.getMediaChegada()) * Math.log(uniforme());
        double tempo_saida = Double.MAX_VALUE;
        long fila = 0;
        long max_fila = 0;

        Litte e_n = new Litte();
        Litte e_w_chegada = new Litte();
        Litte e_w_saida = new Litte();

        while (tempo_decorrido < params.getTempoSimulacao()) {
            tempo_decorrido = min(tempo_chegada, tempo_saida);
            if (tempo_decorrido == tempo_chegada) {
                if (fila == 0) {
                    double tempo_servico = (-1.0 / params.getMediaServico()) * Math.log(uniforme());
                    tempo_saida = tempo_decorrido + tempo_servico;
                }
                fila++;
                max_fila = fila > max_fila ? fila : max_fila;
                tempo_chegada = tempo_decorrido
                        + (-1.0 / params.getMediaChegada()) * Math.log(uniforme());
                // E[N]
                e_n.setSomaAreas(
                        e_n.getSomaAreas() + (tempo_decorrido - e_n.getTempoAnterior()) * e_n.getNoEventos());
                e_n.setNoEventos(e_n.getNoEventos() + 1);
                e_n.setTempoAnterior(tempo_decorrido);
                // E[W] - Chegada
                e_w_chegada.setSomaAreas(
                        e_w_chegada.getSomaAreas()
                                + (tempo_decorrido - e_w_chegada.getTempoAnterior()) * e_w_chegada.getNoEventos());
                e_w_chegada.setNoEventos(e_w_chegada.getNoEventos() + 1);
                e_w_chegada.setTempoAnterior(tempo_decorrido);
            } else if (tempo_decorrido == tempo_saida) {
                // saida
                fila--;
                if (fila != 0) {
                    double tempo_servico = (-1.0 / params.getMediaServico()) * Math.log(uniforme());
                    tempo_saida = tempo_decorrido + tempo_servico;
                } else {
                    tempo_saida = Double.MAX_VALUE;
                }
                e_n.setSomaAreas(e_n.getSomaAreas() + (tempo_decorrido - e_n.getTempoAnterior()) * e_n.getNoEventos());
                e_n.setNoEventos(e_n.getNoEventos() - 1);
                e_n.setTempoAnterior(tempo_decorrido);

                e_w_saida.setSomaAreas(
                        e_w_saida.getSomaAreas()
                                + (tempo_decorrido - e_w_saida.getTempoAnterior()) * e_w_saida.getNoEventos());
                e_w_saida.setNoEventos(e_w_saida.getNoEventos() + 1);
                e_w_saida.setTempoAnterior(tempo_decorrido);
            } else {
                System.out.println("Evento invalido!");
                return;
            }
        }

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