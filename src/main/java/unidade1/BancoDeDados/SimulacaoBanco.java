package unidade1.BancoDeDados;

public class SimulacaoBanco {
    public static void main(String[] args) {
        BancoDeDados db = new BancoDeDados();

        db.create(1, "Dado Inicial");

        System.out.println("--- INICIANDO SIMULAÇÃO ---");

        for (int i = 1; i <= 12; i++) {
            final int idLeitor = i;
            new Thread(() -> {
                db.read(1);
            }, "Leitor-" + idLeitor).start();
        }

        try { Thread.sleep(200); } catch (InterruptedException e) {}

        new Thread(() -> {
            db.update(1, "Valor Alterado");
        }, "ESCRITOR-UPDATE").start();

        new Thread(() -> {
            db.read(1);
        }, "Leitor-Atrasado").start();
    }
}
