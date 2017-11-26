package com.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueSample {
    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        Producer producer = new Producer("Producer", queue, 50);
        Consumer consumer = new Consumer("Consumer", queue, 50);
        producer.start();
        consumer.start();

        Thread.sleep(3000);

        producer.interrupt();
        consumer.interrupt();
    }

    public static class Consumer extends Thread {
        private Queue<Integer> queue;
        private int waitFactor;

        public Consumer(String id, Queue<Integer> queue, int waitFactor) {
            setName(id);
            this.queue = queue;
            this.waitFactor = waitFactor;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                    if (!queue.isEmpty()) {
                        int value = queue.poll();
                        System.out.println(String.format("Value = %s", value));
                        System.out.println(String.format("Removed 1, size = %s", queue.size()));
                    } else {
                        System.out.println("Queue is empty");
                        Thread.sleep(1 + (int) (Math.random() * waitFactor));
                    }
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                } catch (InterruptedException e) {
                    interrupt();
                    System.out.println(this + " end");
                    break;
                }
            }
        }
    }

    public static class Producer extends Thread {
        private Queue<Integer> queue;
        private int waitFactor;

        public Producer(String id, Queue<Integer> queue, int waitFactor) {
            setName(id);
            this.queue = queue;
            this.waitFactor = waitFactor;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("=================================================");
                    queue.offer(1);
                    System.out.println(String.format("Offer 1, size = %s", queue.size()));
                    Thread.sleep(1 + (int) (Math.random() * waitFactor));
                    System.out.println("=================================================");
                } catch (InterruptedException e) {
                    interrupt();
                    System.out.println(this + " end");
                    break;
                }
            }
        }
    }
}
