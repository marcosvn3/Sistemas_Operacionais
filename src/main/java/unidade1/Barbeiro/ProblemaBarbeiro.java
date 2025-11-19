package unidade1.Barbeiro;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;


public class ProblemaBarbeiro {

    private static final int CHEGADA_CLIENTE_MIN = 3000;
    private static final int CHEGADA_CLIENTE_MAX = 7000;

    private static final int LIMITE_FILA = 10;

    public static void main(String[] args) {
        BlockingQueue<Cliente> filaDeEspera = new ArrayBlockingQueue<>(LIMITE_FILA);

        System.out.println("--- Barbearia Aberta ---");
        System.out.println("Cadeiras de espera: " + LIMITE_FILA);

        Thread barbeiro = new Thread(new Barbeiro(filaDeEspera));
        barbeiro.start();


        int idCliente = 1;
        while (true) {
            try {
                long tempoChegada = ThreadLocalRandom.current().nextLong(CHEGADA_CLIENTE_MIN, CHEGADA_CLIENTE_MAX);
                Thread.sleep(tempoChegada);

                Cliente novoCliente = new Cliente(idCliente++);
                System.out.println("\n[CHEGADA] " + novoCliente + " chegou na barbearia.");

                boolean conseguiuEntrar = filaDeEspera.offer(novoCliente);

                if (conseguiuEntrar) {
                    System.out.println(" -> " + novoCliente + " sentou na fila de espera. (Fila: " + filaDeEspera.size() + "/10)");
                } else {
                    System.out.println(" -> [LOTADO] Fila cheia! " + novoCliente + " foi embora furioso.");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}



