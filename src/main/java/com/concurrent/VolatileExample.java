package com.concurrent;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class VolatileExample {

    public static void main(String[] args) {
        VolatileExample executor = new VolatileExample();
        executor.execute();
    }

    public void execute() {
        SharedCounter sharedCounter = new SharedCounter();

        List<CountWorker> workers = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            CountWorker worker = new CountWorker(i, 1000, sharedCounter);
            workers.add(worker);
            worker.start();
        }

        workers.forEach(worker -> {
            try {
                worker.join();
            } catch (InterruptedException ignore) {
            }
        });

        System.out.println(sharedCounter.count);
    }

    class SharedCounter {
        /**
         * jmm обеспечивает
         * гарантированно happens-before запись перед тем как другие потоки попытаются прочитать эту переменную
         */
        private volatile long count;

        long increment() {
            return ++count;
        }

        long getCount() {
            return count;
        }
    }

    class CountWorker extends Thread {
        private int id;
        private int limit;
        private SharedCounter sharedCounter;

        public CountWorker(int id, int limit, SharedCounter sharedCounter) {
            this.id = id;
            this.limit = limit;
            this.sharedCounter = sharedCounter;
        }

        @Override
        public void run () {
            sleep();

            while (sharedCounter.getCount() != limit) {
                System.out.println(
                        format(
                                "Worker with id '%s' increment count '%s'",
                                id,
                                sharedCounter.increment()
                        )
                );
            }
        }

        private void sleep() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
