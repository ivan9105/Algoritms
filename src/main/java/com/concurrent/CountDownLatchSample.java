package com.concurrent;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchSample {
    private static final CountDownLatch LATCH = new CountDownLatch(11);
    private static final int THRESHOLD = 5000;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 8; i++) {
            new Thread(new PlaceHolder(i)).start();
            Thread.sleep(1000);
        }

        while (LATCH.getCount() > 3) {
            Thread.sleep(100);
        }

        Thread.sleep(1000);
        System.out.println("Operation 1");
        LATCH.countDown();
        Thread.sleep(1000);
        System.out.println("Operation 2");
        LATCH.countDown();
        Thread.sleep(1000);
        System.out.println("Threads start");
        LATCH.countDown();
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
                LATCH.countDown();
                LATCH.await();
                Thread.sleep(THRESHOLD);
                System.out.println(String.format("Place holder '%s' releases", id));
            } catch (InterruptedException ignore) {
            }
        }
    }
}
