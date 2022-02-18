package com.sweettyy.myclasses;

import com.sweettyy.main;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Hash {

    String content;
    String password;
    char[] symbols;
    LinkedList<String> sharedQueue = new LinkedList<>();
    int size = 10000;
    Thread prodThread;
    Thread[] consThread;


    //    * Конструкторы
    public Hash() {
        content = "";
    }

    Hash(String s) {
        content = s;
    }

    //    * Геттеры и сеттеры
    public void setContent(String s) {
        this.content = s;
    }
    String getContent() {
        return this.content;
    }

    public void setPassword(String p) {
        this.password = p;
    }
    String getPassword() {
        return this.password;
    }

    //    * Функция для вычисления хэша


    //    * Заполняем алфавит от a до z
    public void init() {
//        * Размер алфавита
        final int ALPHAVITE_SIZE = 26;
        symbols = new char[ALPHAVITE_SIZE];
        for (char c = 'a'; c <= 'z'; c++) symbols[c - '0' - 49] = c;
    }

    //    * Функция для подбора пароля
    public void passwordSelection(int streamCount) throws NoSuchAlgorithmException, InterruptedException {
        this.init();
        streamCount = Math.max(1, streamCount);

        this.consThread = new Thread[streamCount];
        for(int i = 0; i < streamCount; i++){
            this.consThread[i] = new Thread(new Consumer(this.sharedQueue, this.content), "Consumer");
        }
        this.prodThread = new Thread(new Producer(this.sharedQueue, this.size, this.symbols), "Producer");

        prodThread.start();
        for(Thread ct : consThread){
            ct.start();
        }

    }
}
