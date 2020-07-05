package com.jmm;

import lombok.SneakyThrows;

import static java.lang.Runtime.getRuntime;
import static java.lang.Thread.sleep;

public class FinalizeUsage {

    private static Runtime runtime = getRuntime();

    @SneakyThrows
    public static void main(String[] args) {
        createToBigObjectLoop();
    }

    /**
     * run with -Xms10m -Xmx10m
     * @throws InterruptedException
     */
    private static void createToBigObjectLoop() throws Throwable {
        int i = 0;
        while (true) {
            new ObjectTooLongFinalizeImplementation();
            new CharArray();
            sleep(10);

            if (i++ % 100 == 0) {
                System.out.println("Total: " + runtime.totalMemory() +
                        "; free: " + runtime.freeMemory());
            }
        }
    }

    static class CharArray {
        char[] arr = new char[10000];

        @Override
        protected void finalize() throws Throwable {
            System.out.println("Finalize");
        }
    }

    /**
     * Объекты у которых переопределен finalize method
     * попадают в java.lang.ref.Finalizer.FinalizerThread
     * и обрабатываются в порядке очереди
     * если эта очередь будет забита
     * сборщик мусора долго не сможет удалять эти объекты из Heap
     */
    static class ObjectTooLongFinalizeImplementation {

        @Override
        protected void finalize() throws Throwable {
            System.out.println("Call ObjectTooLongFinalizeImplementation#finalize()");
            Thread.sleep(1000000);
        }
    }
}
