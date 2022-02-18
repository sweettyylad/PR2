package com.sweettyy.myclasses;

import com.sweettyy.main;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;

// implements Runnable чтобы запускать в отдельном потоке
public class Producer implements Runnable {

    char[] symbols;
    private final Queue<String> sharedQueue;
    private final int SIZE;
    private final int MAX_KEY_SIZE = 5;

    // Конструктор
    public Producer(Queue<String> sharedQueue, int size, char[] s) {
        this.sharedQueue = sharedQueue;
        this.SIZE = size;
        this.symbols = s.clone();
    }



    @Override
    public void run() {
        // Цикл бесконечен
        StringBuilder selected = new StringBuilder();
        char[] trial = new char[MAX_KEY_SIZE];
        for (int z = 0; z < MAX_KEY_SIZE; z++) {
            trial[z] = (char) (0 + 'a');
        }

        while (true) {
            selected = new StringBuilder("");
            for (int z = 0; z < MAX_KEY_SIZE; z++) {
                selected.append(trial[z]);
            }
            try {
                if(main.find){return;}
                produce(selected);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            trial[MAX_KEY_SIZE - 1] = (char) (trial[MAX_KEY_SIZE - 1] + 1);
            for (int k = MAX_KEY_SIZE - 1; k > 0; k--) {
                if ((trial[k] - '0' - 49) > symbols.length - 1) {
                    trial[k - 1] = (char) (trial[k - 1] + 1);
                    trial[k] = 'a';
                }
            }
        }
    }

    private void produce(StringBuilder selected) throws InterruptedException {
        synchronized (sharedQueue) { // обязательно synchronized

            if (sharedQueue.size() == SIZE) {
                // Если очередь полна, то ждём
                sharedQueue.wait();
            }

            // Добавили элемент в очередь.
            sharedQueue.add(selected.toString());

            // Уведомили другой поток на случай, если он ждет
            sharedQueue.notifyAll();
        }
    }
}