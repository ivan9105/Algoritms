package com.concurrent.pool;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;

/**
 * Подзабыл на собесе про LinkedBlockingQueue
 */
public class FixedThreadPoolUsage {
    public static void main(String[] args) {
        var threadPool = Executors.newFixedThreadPool(4);

        var workers = new ArrayList<Runnable>();

        for (int index = 0; index < 10; index++) {
            int copyIndex = index;
            workers.add(() -> {
                System.out.println("Start runner: " + copyIndex);
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException ignore) {
                }
                System.out.println("Finish runner: " + copyIndex);
            });
        }

        workers.forEach(threadPool::execute);

        //без этого блокируется текущий поток
        threadPool.shutdown();

        System.out.println("Finish process");
    }
}
