package com.concurrent;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * Подзабыл на собесе про LinkedBlockingQueue
 * блокирует по факту текущий поток исполнения
 * fixed with pool size 1
 */
public class SingletonThreadPoolUsage {
    public static void main(String[] args) {
        var threadPool = Executors.newSingleThreadExecutor();

        var workers = new ArrayList<Runnable>();

        for (int index = 0; index < 10; index++) {
            int copyIndex = index;
            workers.add(() -> {
                System.out.println("Start runner: " + copyIndex);
                try {
                    Thread.sleep(50L);
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
