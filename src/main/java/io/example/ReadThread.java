package io.example;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static io.example.Program.*;

public class ReadThread implements Runnable {
    private Integer readerRate = 400;
    Integer count;
    public ReadThread(Integer count) {
        this.count = count;
    }
    @Override
    public void run() {
        TimerTask readerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    readerSemaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Random random = new Random();
                int index = random.nextInt(BuffersPool.size());
                while(BuffersPool.get(index).toString().isEmpty()){
                    index = random.nextInt(BuffersPool.size());
                }
                String dataFromBuffer =
                        BuffersPool.get(index).toString();
                System.out.println("Из буфера " + (index+1) + " была считана следующая запись: " + dataFromBuffer);
                BuffersPool.set(index, new StringBuffer());
                writerSemaphore.release();
            }
        };
        Timer timer = new Timer();
        timer.schedule(readerTask, 40, readerRate);
    }
}
