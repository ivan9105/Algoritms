package com.concurrent.pool;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * тоже забыл на собесе по факту внутри SynchronousQueue она не блокирует текущий поток
 * автоматически создает и удаляет worker-ы в отличие от fixed thread pool
 */
public class CachedThreadPoolUsage {
    public static void main(String[] args) {
        var threadPool = Executors.newCachedThreadPool();

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

        //можно не делать текущий поток не блокируется
        threadPool.shutdown();

        System.out.println("Finish process");
    }
}
