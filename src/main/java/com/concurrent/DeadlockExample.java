package com.concurrent;

public class DeadlockExample {
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void foo() {
        synchronized (lock1) {
            synchronized (lock2) {
                System.out.println("foo");
            }
        }
    }

    public void bar() {
        synchronized (lock2) {
            synchronized (lock1) {
                System.out.println("bar");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            DeadlockExample business = new DeadlockExample();

            for (int i = 0; i < 100; i++) {
                new Thread(business::foo).start();
            }

            for (int i = 0; i < 100; i++) {
                new Thread(business::bar).start();
            }

            System.out.println("===================================");
            //не ловиться или очень редко ловиться по коду должно

            //чтобы избежать нужно в #bar локи местами поменять
        }
    }

    private static void firstSample() throws InterruptedException {
        while (true) {
            DeadlockExample business = new DeadlockExample();
            new Thread(business::foo).start();
            new Thread(business::bar).start();
            Thread.sleep(500L);
            //But deadlock is likely never to occur because one thread can execute and exit a method very quickly so the other thread have chance to acquire the locks.
        }
    }
}
