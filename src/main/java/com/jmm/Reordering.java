package com.jmm;

public class Reordering {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {

        while (true) {

            synchronized (Reordering.class) {
                a = 0;
                b = 0;
                x = 0;
                y = 0;
            }

            /**
             * Операции внутри потоков могут превращаться в байткоде в разную последовательность действий
             */
            Thread p = new Thread(() -> {
                a = 1;
                x = b;
            });

            Thread q = new Thread(() -> {
                b = 1;
                y = a;
            });

            p.start();
            q.start();
            p.join();
            q.join();

            System.out.println("x=" + x + ", y=" + y);

            Thread.sleep(1000L);
        }
    }
}
