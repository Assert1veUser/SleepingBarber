package io.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static io.example.Program.*;

public class WriteThread implements Runnable {
    private Integer writerRate = 300;
    Integer count;
    public WriteThread(Integer count) {
        this.count = count;
    }
    @Override
    public void run() {
        TimerTask writerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    writerSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Random rand = new Random();
                int index = rand.nextInt(BuffersPool.size());
                while(!BuffersPool.get(index).toString().isEmpty()){
                    index = rand.nextInt(BuffersPool.size());
                }
                String stringToWrite = "Новая строка " +
                        LocalDateTime.now().format(DateTimeFormatter.ISO_TIME);
                BuffersPool.set(index, new
                        StringBuffer(stringToWrite));
                System.out.println("В буфер " + (index+1) + " была записана запись " + stringToWrite);
                        readerSemaphore.release();
            }
        };
        Timer timer = new Timer();
        timer.schedule(writerTask, 0, writerRate);
    }
}
