package unidade1.Barbeiro;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

class Barbeiro implements Runnable {
    private final BlockingQueue<Cliente> fila;

    public Barbeiro(BlockingQueue<Cliente> fila) {
        this.fila = fila;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (fila.isEmpty()) {
                    System.out.println("  (Barbeiro está dormindo na cadeira Zzz...)");
                }

                Cliente clienteAtual = fila.take();

                System.out.println("  [ATENDIMENTO] Barbeiro acordou e está cortando cabelo do " + clienteAtual);

                long tempoCorte = ThreadLocalRandom.current().nextLong(1000, 10001);
                Thread.sleep(tempoCorte);

                System.out.println("  [FINALIZADO] " + clienteAtual + " cortou o cabelo em " + (tempoCorte/1000.0) + "s e saiu.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Barbeiro encerrou o expediente.");
                break;
            }
        }
    }
}