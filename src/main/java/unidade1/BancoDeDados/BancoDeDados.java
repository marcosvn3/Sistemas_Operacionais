package unidade1.BancoDeDados;

import java.util.HashMap;
import java.util.Map;


class BancoDeDados {
    private final Map<Integer, String> dados = new HashMap<>();


    private int leitoresAtivos = 0;
    private boolean escrevendo = false;

    private final int MAX_LEITORES = 10;



    private synchronized void iniciarLeitura() throws InterruptedException {

        while (escrevendo || leitoresAtivos >= MAX_LEITORES) {
            System.out.println(" -> " + Thread.currentThread().getName() + " está ESPERANDO para ler (Ocupado ou Cheio).");
            wait();
        }
        leitoresAtivos++;
        System.out.println(Thread.currentThread().getName() + " iniciou leitura. Leitores ativos: " + leitoresAtivos);
    }

    private synchronized void finalizarLeitura() {
        leitoresAtivos--;
        if (leitoresAtivos == 0) {
            notifyAll();
        } else if (leitoresAtivos < MAX_LEITORES) {
            notifyAll();
        }
    }

    private synchronized void iniciarEscrita() throws InterruptedException {
        while (escrevendo || leitoresAtivos > 0) {
            System.out.println(" -> " + Thread.currentThread().getName() + " está ESPERANDO para escrever.");
            wait();
        }
        escrevendo = true;
        System.out.println(">>> " + Thread.currentThread().getName() + " iniciou ESCRITA (Bloqueio total).");
    }

    private synchronized void finalizarEscrita() {
        escrevendo = false;
        System.out.println("<<< " + Thread.currentThread().getName() + " finalizou ESCRITA.");
        notifyAll();
    }




    public void create(int id, String valor) {
        try {
            iniciarEscrita();
            // Simula tempo de operação
            Thread.sleep(1000);
            dados.put(id, valor);
            System.out.println("    [INSERT] Registro " + id + " criado.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            finalizarEscrita();
        }
    }


    public String read(int id) {
        try {
            iniciarLeitura();

            Thread.sleep(500);
            String valor = dados.get(id);
            System.out.println("    [SELECT] Lendo id " + id + ": " + valor);
            return valor;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            finalizarLeitura();
        }
    }

    public void update(int id, String novoValor) {
        try {
            iniciarEscrita();
            Thread.sleep(1000);
            if (dados.containsKey(id)) {
                dados.put(id, novoValor);
                System.out.println("    [UPDATE] Registro " + id + " atualizado.");
            } else {
                System.out.println("    [UPDATE] Registro " + id + " não encontrado.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            finalizarEscrita();
        }
    }

    public void delete(int id) {
        try {
            iniciarEscrita();
            Thread.sleep(1000);
            dados.remove(id);
            System.out.println("    [DELETE] Registro " + id + " removido.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            finalizarEscrita();
        }
    }
}