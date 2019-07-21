package com.example.easyexcel.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolServiceImpl {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(200,1000,300, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>());

    private static SynchronizedInteger val = new SynchronizedInteger();

    private static volatile AtomicInteger count = new AtomicInteger(0);

    public static   void threadByOnlyValue(){
        CountDownLatch latch = new CountDownLatch(50);
        for (int i = 0;i < 50;i++ ){
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        writeValue(latch);
                    }
                });

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        readValue(latch);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private static void writeValue(CountDownLatch latch) {
        val.setValue(val.getValue() +1);
        Thread t = Thread.currentThread();
        count.getAndAdd(1);
        latch.countDown();
        System.out.println("WRITE---线程："+t.getId()+"中 value:"+val.getValue()+"count值："+count.get());
    }

    private static void readValue(CountDownLatch latch){
        try {
            latch.await();
            Thread t = Thread.currentThread();
            System.out.println("READ---线程："+t.getId()+"中 value:"+val.getValue()+"count值："+count.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        threadByOnlyValue();
    }
}
