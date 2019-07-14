package com.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;

public class AtomicSyncExample {

    public static void main(String[] args) {
        AtomicSyncExample executor = new AtomicSyncExample();
        executor.execute();
    }

    private void execute() {
        AtomicCounter sharedCounter = new AtomicCounter();

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

        System.out.println(sharedCounter.getCount());
    }

    class CountWorker extends Thread {
        private int id;
        private int limit;
        private AtomicCounter sharedCounter;

        CountWorker(int id, int limit, AtomicCounter sharedCounter) {
            this.id = id;
            this.limit = limit;
            this.sharedCounter = sharedCounter;
        }

        @Override
        public void run() {
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

    private class AtomicCounter {
        private AtomicLong counter = new AtomicLong();

        long getCount() {
            return counter.get();
        }

        long increment() {
            while (true) {
                long currentValue = getCount();

                /**
                 * здесь критическая секция на основе не блокирующей синхронизации
                 * начало
                 */

                long newValue = currentValue + 1;

                /**
                 * конец
                 */

                //created happens-before orderings
                //если значение current == new в таком случае compare and set return false
                if (counter.compareAndSet(currentValue, newValue)) {
                    return newValue;
                }
            }
        }
    }
}
