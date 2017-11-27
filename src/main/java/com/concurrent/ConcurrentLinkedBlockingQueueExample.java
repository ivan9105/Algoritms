package com.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

public class ConcurrentLinkedBlockingQueueExample {
    public static void main(String[] args) {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(200);

        Producer producer1 = new Producer(queue);
        producer1.start();
        new ReadConsumer(queue, producer1).start();
        new RemoveConsumer(queue, producer1).start();

        Producer producer2 = new Producer(queue);
        producer2.start();
        new ReadConsumer(queue, producer2).start();
        new RemoveConsumer(queue, producer2).start();
    }

    public static class Producer extends Thread {
        private LinkedBlockingQueue<Integer> queue;
        private boolean isRunning;

        public Producer(LinkedBlockingQueue<Integer> queue) {
            this.queue = queue;
            this.isRunning = true;
        }

        @Override
        public void run() {
            for (int i = 0; i < 15; i++) {
                try {
                    queue.put(i);
                    System.out.println(String.format("[PR]Put element: %s", i));
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
            isRunning = false;
        }

        public boolean isRunning() {
            return isRunning;
        }
    }

    public static class ReadConsumer extends Thread {
        private LinkedBlockingQueue<Integer> queue;
        private Producer producer;

        public ReadConsumer(LinkedBlockingQueue<Integer> queue, Producer producer) {
            this.queue = queue;
            this.producer = producer;
        }

        @Override
        public void run() {
            while (producer.isRunning()) {
                System.out.println(String.format("[READ]All elements: %s", queue));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignore) {
                }
            }
            System.out.println("[READ]Reading completed");
            System.out.println();
        }
    }

    public static class RemoveConsumer extends Thread {
        private LinkedBlockingQueue<Integer> queue;
        private Producer producer;

        public RemoveConsumer(LinkedBlockingQueue<Integer> queue, Producer producer) {
            this.queue = queue;
            this.producer = producer;
        }

        @Override
        public void run() {
            while (producer.isRunning()) {
                try {
                    System.out.println(String.format("[REMOVE]Removing element: %s", queue.take()));
                    Thread.sleep(2000);
                } catch (InterruptedException ignore) {
                }

            }
        }
    }
}
