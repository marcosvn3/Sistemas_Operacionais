package unidade1.RedimensionarImagem;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class RedimensionadorImagens {


    private static final double FATOR_ESCALA = 0.65;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Redimensionador de Imagens Multi-Thread ---");
        System.out.print("Digite o caminho da pasta com as imagens: ");
        String caminhoPasta = scanner.nextLine();

        File pastaOrigem = new File(caminhoPasta);

        if (!pastaOrigem.exists() || !pastaOrigem.isDirectory()) {
            System.out.println("Erro: Pasta não encontrada ou caminho inválido.");
            return;
        }

        File[] arquivosImagens = pastaOrigem.listFiles((dir, name) -> {
            String low = name.toLowerCase();
            return low.endsWith(".jpg") || low.endsWith(".png") || low.endsWith(".jpeg");
        });

        if (arquivosImagens == null || arquivosImagens.length == 0) {
            System.out.println("Nenhuma imagem encontrada na pasta.");
            return;
        }

        File pastaDestino = new File(pastaOrigem, "redimensionadas");
        if (!pastaDestino.exists()) {
            pastaDestino.mkdir();
        }

        System.out.println("Encontradas " + arquivosImagens.length + " imagens.");
        System.out.println("Iniciando processamento com " + NUM_THREADS + " threads...");

        long inicio = System.currentTimeMillis();


        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (File imgFile : arquivosImagens) {

            executor.submit(() -> processarImagem(imgFile, pastaDestino));
        }


        executor.shutdown();
        try {
            // Aguarda até que todas terminem (timeout alto para garantir)
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            System.err.println("Processamento interrompido.");
        }

        long fim = System.currentTimeMillis();
        System.out.println("--- Concluído! ---");
        System.out.println("Tempo total: " + (fim - inicio) + "ms");
        System.out.println("Imagens salvas em: " + pastaDestino.getAbsolutePath());
    }

    private static void processarImagem(File arquivoOriginal, File pastaDestino) {
        try {
            String nomeArquivo = arquivoOriginal.getName();

            String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);


            BufferedImage imagemOriginal = ImageIO.read(arquivoOriginal);
            if (imagemOriginal == null) return;


            int larguraOriginal = imagemOriginal.getWidth();
            int alturaOriginal = imagemOriginal.getHeight();
            int novaLargura = (int) (larguraOriginal * FATOR_ESCALA);
            int novaAltura = (int) (alturaOriginal * FATOR_ESCALA);


            int tipoImagem = (imagemOriginal.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : imagemOriginal.getType();
            BufferedImage imagemRedimensionada = new BufferedImage(novaLargura, novaAltura, tipoImagem);

            Graphics2D g = imagemRedimensionada.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(imagemOriginal, 0, 0, novaLargura, novaAltura, null);
            g.dispose();

            File arquivoSaida = new File(pastaDestino, nomeArquivo);
            ImageIO.write(imagemRedimensionada, extensao, arquivoSaida);

            System.out.println(Thread.currentThread().getName() + " processou: " + nomeArquivo);

        } catch (IOException e) {
            System.err.println("Erro ao processar " + arquivoOriginal.getName() + ": " + e.getMessage());
        }
    }
}