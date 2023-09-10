package com.concurrent.pool;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Похоже на cached thread pool executor
 * Ток отложенно запускает таску
 */
public class ScheduledThreadPoolUsage {
    public static void main(String[] args) {
        var threadPool = Executors.newScheduledThreadPool(10);

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

        workers.forEach(it -> threadPool.schedule(it, 300, TimeUnit.MILLISECONDS));

        //без этого блокируется текущий поток
        threadPool.shutdown();

        System.out.println("Finish process");
    }
}
