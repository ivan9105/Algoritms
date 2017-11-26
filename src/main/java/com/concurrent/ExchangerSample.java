package com.concurrent;

import java.util.concurrent.Exchanger;

public class ExchangerSample {
    private static final Exchanger<String> EXCHANGER = new Exchanger<>();

    public static void main(String[] args) throws InterruptedException {
        String[] exchangePoint1 = new String[] { "package a->d", "package a->c"};
        String[] exchangePoint2 = new String[] { "package b->c", "package b->d"};
        new Thread(new Process(0, "a", "d", exchangePoint1)).start();
        Thread.sleep(1000);
        new Thread(new Process(1, "b", "c", exchangePoint2)).start();
    }

    public static class Process implements Runnable {
        private int id;
        private String startPoint;
        private String exchangePoint;
        private String[] exchangeData;

        public Process(int id, String startPoint, String exchangePoint, String[] exchangeData) {
            this.id = id;
            this.startPoint = startPoint;
            this.exchangePoint = exchangePoint;
            this.exchangeData = exchangeData;
        }

        @Override
        public void run() {
            try {
                System.out.println(String.format("Process '%s' is ready", id));
                System.out.println(String.format("Process '%s' runs from point '%s' to point '%s'", id, startPoint, exchangePoint));
                Thread.sleep(1000 + (long) (Math.random() * 5000));
                System.out.println(String.format("Process '%s' in point '%s'", id, exchangePoint));
                exchangeData[0] = EXCHANGER.exchange(exchangeData[0]);
                System.out.println(String.format("In process '%s' move exchange data for point '%s'", id, exchangePoint));
                Thread.sleep(1000 + (long) (Math.random() * 5000));
                System.out.println(String.format("Process '%s' releases to point '%s' and delivered: '%s' and '%s'",
                        id, exchangePoint, exchangeData[0], exchangeData[1]));
            } catch (InterruptedException ignore) {
            }
        }
    }
}
