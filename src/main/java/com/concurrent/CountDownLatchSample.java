package com.concurrent;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

public class CountDownLatchSample {
    //TODO здесь остановился
    //TODO Lock support Park
    //TOdo time adjuster
    //Todo treefying дерева разобраться поподробнее


    //TODO кольцо кролика на низком уровне
    /*
    создать две точки доступа: WorkExchange и RetryExchange;
создать очередь WorkQueue с параметрами x-dead-letter-exchange=RetryExchange и связать ее c WorkExchange;
создать очередь RetryQueue с параметрами x-dead-letter-exchange=WorkExchange и x-message-ttl=300000, и связать ее c RetryExchange.
     */
    private static final CountDownLatch LATCH = new CountDownLatch(3);
    private static final int THRESHOLD = 500;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            new Thread(new PlaceHolder(i)).start();
            sleep(300);
        }

        System.out.println("Operation 1");
        LATCH.countDown();
        sleep(1000);
        System.out.println("Operation 2");
        LATCH.countDown();
        sleep(1000);
        LATCH.countDown();
        System.out.println("Threads continue execution");
    }

    public static class PlaceHolder implements Runnable {
        private int id;

        public PlaceHolder(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println(String.format("Place holder '%s' is ready", id));
                LATCH.await();
                sleep(THRESHOLD);
                System.out.println(String.format("Place holder '%s' releases", id));
            } catch (InterruptedException ignore) {
            }
        }
    }
}
