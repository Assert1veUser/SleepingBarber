package io.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Program {
    public static Semaphore writerSemaphore;
    public static Semaphore readerSemaphore;
    public static List<StringBuffer> BuffersPool;
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите количество буферов в пуле");
        String readedLine = reader.readLine();
        reader.close();
        int n = Integer.parseInt(readedLine);
        readerSemaphore = new Semaphore(n);
        writerSemaphore = new Semaphore(n);
        try {
            writerSemaphore.acquire(n);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        readerSemaphore.release(0);
        BuffersPool = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String str = "Стартовая запись " + (i + 1);
            BuffersPool.add(new StringBuffer(str));
        }
        WriteThread writeThread = new WriteThread(n);
        ReadThread readThread = new ReadThread(n);
        new Thread(writeThread).start();
        new Thread(readThread).start();
    }
}
