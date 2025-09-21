package com.concurrent.synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierSample {
    private static final CyclicBarrier BARRIER = new CyclicBarrier(3, new PlaceHoldersOperation());

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 9; i++) {
            new Thread(new PlaceHolder(i)).start();
            Thread.sleep(400);
        }
    }

    public static class PlaceHoldersOperation implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                System.out.println("Operation done");
            } catch (InterruptedException ignore) {
            }
        }
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
                BARRIER.await();
                System.out.println(String.format("Place holder '%s' do process", id));
            } catch (InterruptedException | BrokenBarrierException ignore) {
            }
        }
    }
}
