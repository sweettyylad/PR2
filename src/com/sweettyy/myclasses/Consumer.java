package com.sweettyy.myclasses;

import com.sweettyy.main;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;

// implements Runnable чтобы запускать в отдельном потоке
public class Consumer implements Runnable {
    // Общая очередь
    private final Queue<String> sharedQueue;
    String content;
    String answer;
    public Consumer(Queue<String> sharedQueue, String c) {
        this.sharedQueue = sharedQueue;
        this.content = c;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String check = consume();
//                System.out.println("Принятие сгенерированного: " + check);
                if (hash(check).equals(this.content)) {
                    System.out.println("Done! Your Pass - " + check);
                    main.find = true;
                    System.out.println("Программа выполнялась приблизительно " + ((System.currentTimeMillis() - main.time)/1000) + "s");
                    this.answer = check;
                }
                if(main.find){
                    synchronized (sharedQueue){
                        sharedQueue.notifyAll();
                    }
                    return;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public static String hash(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Метод, извлекающий элементы из общей очереди
    private String consume() throws InterruptedException {
        synchronized (sharedQueue) {
            if (sharedQueue.isEmpty()) { // Если пуста, надо ждать
                sharedQueue.wait();
            }
            sharedQueue.notifyAll();
            return sharedQueue.poll();
        }
    }
}