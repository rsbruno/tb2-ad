import java.util.Random;

class Parametros1 {
    double mediaChegada;
    double mediaServico;
    double tempoSimulacao;

    Parametros1() {
        mediaChegada = 1.0 / 0.1;
        mediaServico = 1.0 / 0.06;
        tempoSimulacao = 1200;
    }
}

class Littlew {
    long noEventos;
    double tempoAnterior;
    double somaAreas;

    Littlew() {
        noEventos = 0;
        tempoAnterior = 0.0;
        somaAreas = 0.0;
    }
}

public class ChatGPT {

    static Random random = new Random();

    static double uniforme() {
        double u = random.nextDouble();
        // limitando entre (0,1]
        u = 1.0 - u;
        return u;
    }

    static double min(double d1, double d2) {
        return d1 < d2 ? d1 : d2;
    }

    static void leParametros(Parametros1 params) {
        System.out.print("Informe o tempo medio entre clientes (s): ");
        params.mediaChegada = 1.0 / Double.parseDouble(System.console().readLine());

        System.out.print("Informe o tempo medio de servico (s): ");
        params.mediaServico = 1.0 / Double.parseDouble(System.console().readLine());

        System.out.print("Informe o tempo a ser simulado (s): ");
        params.tempoSimulacao = Double.parseDouble(System.console().readLine());
    }

    public static void main(String[] args) {

        random.setSeed(864000);

        // le valores parametrizados
        Parametros1 params = new Parametros1();

        // System.out.println(params.mediaChegada);

        // variaveis de controle da simulacao
        double tempoDecorrido = 0.0;
        double tempoChegada = (-1.0 / params.mediaChegada) * Math.log(uniforme());
        double tempoSaida = Double.MAX_VALUE;
        long fila = 0;
        long maxFila = 0;

        // variaveis de medidas de interesse
        double somaOcupacao = 0.0;

        /**
         * Little
         */
        Littlew eN = new Littlew();
        Littlew eWChegada = new Littlew();
        Littlew eWSaida = new Littlew();

        while (tempoDecorrido < params.tempoSimulacao) {
            tempoDecorrido = min(tempoChegada, tempoSaida);

            if (tempoDecorrido == tempoChegada) {
                // chegada
                if (fila == 0) {
                    double tempoServico = (-1.0 / params.mediaServico) * Math.log(uniforme());

                    tempoSaida = tempoDecorrido + tempoServico;

                    somaOcupacao += tempoServico;
                }
                fila++;
                maxFila = fila > maxFila ? fila : maxFila;

                tempoChegada = tempoDecorrido + (-1.0 / params.mediaChegada) * Math.log(uniforme());

                // calculo little -- E[N]
                eN.somaAreas += (tempoDecorrido - eN.tempoAnterior) * eN.noEventos;
                eN.noEventos++;
                eN.tempoAnterior = tempoDecorrido;

                // calculo little -- E[W] - chegada
                eWChegada.somaAreas += (tempoDecorrido - eWChegada.tempoAnterior) * eWChegada.noEventos;
                eWChegada.noEventos++;
                eWChegada.tempoAnterior = tempoDecorrido;

            } else if (tempoDecorrido == tempoSaida) {
                // saida
                fila--;
                if (fila > 0) {
                    double tempoServico = (-1.0 / params.mediaServico) * Math.log(uniforme());

                    tempoSaida = tempoDecorrido + tempoServico;

                    somaOcupacao += tempoServico;
                } else {
                    tempoSaida = Double.MAX_VALUE;
                }

                // calculo little -- E[N]
                eN.somaAreas += (tempoDecorrido - eN.tempoAnterior) * eN.noEventos;
                eN.noEventos--;
                eN.tempoAnterior = tempoDecorrido;

                // calculo little -- E[W] - saida
                eWSaida.somaAreas += (tempoDecorrido - eWSaida.tempoAnterior) * eWSaida.noEventos;
                eWSaida.noEventos++;
                eWSaida.tempoAnterior = tempoDecorrido;

            } else {
                System.out.println("Evento invalido!");
                return;
            }
        }

        System.out.println("chegada: " + eWChegada.somaAreas);
        System.out.println("tempoAnterior: " + eWSaida.tempoAnterior);
        System.out.println("noEventos: " + eWSaida.noEventos);

        eWChegada.somaAreas += (tempoDecorrido - eWChegada.tempoAnterior) * eWChegada.noEventos;

        eWSaida.somaAreas += (tempoDecorrido - eWSaida.tempoAnterior) * eWSaida.noEventos;

        System.out.printf("ocupacao: %.6f\n", somaOcupacao / tempoDecorrido);
        System.out.printf("tamanho maximo da fila: %d\n", maxFila);

        double eNCalculo = eN.somaAreas / tempoDecorrido;
        double eWCalculo = (eWChegada.somaAreas - eWSaida.somaAreas) / eWChegada.noEventos;
        double lambda = eWChegada.noEventos / tempoDecorrido;

        System.out.printf("E[N]: %.6f\n", eNCalculo);
        System.out.printf("E[W]: %.6f\n", eWCalculo);
        System.out.printf("Erro de Little: %.20f\n", Math.abs(eNCalculo - lambda * eWCalculo));
    }
}