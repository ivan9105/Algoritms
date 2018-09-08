package com.certificate.concurrency;

import org.junit.Test;

public class ConcurrencyMockTest {

    @Test
    public void joinWithoutCheckInterruptedException() {
        Thread thread = new Thread(() -> {
        });
        thread.start();
        System.out.print("Begin");
        //a.join(); must catch InterruptedException, it's throw compilation fails
        System.out.print("End");
    }

    @Test
    public void unknownResult1() {
        class Cruiser {
            private int val = 0;

            class LittleCruiser implements Runnable {
                @Override
                public void run() {
                    int current;
                    for (int i = 0; i < 4; i++) {
                        current = val;
                        System.out.println(current + ", ");
                        val = current + 2;
                    }
                }
            }

            private void foo() {
                Runnable runnable = new LittleCruiser();
                new Thread(runnable).start();
                new Thread(runnable).start();
            }
        }

        new Cruiser().foo();
        //? cause can not synchronized
    }

    /**
     * result BeginRunEnd
     * maybe, cause start too long operation
     * Thread sleep 1 second given result RunBeginEnd for example
     */
    @Test
    public void joinWithCheckInterruptedException() throws InterruptedException {
        Thread thread = new Thread(() -> System.out.println("Run"));
        thread.start();
        System.out.println("Begin");
        thread.join();
        System.out.println("End");
    }

    @Test
    public void waitWithoutCheckInterruptedException() {
        Object object = new Object();
        synchronized (object) {
//            object.wait(); must catch InterruptedException, it's throw compilation fails
            object.notify();
        }
    }

    @Test
    public void callRunThreadMethod() {
        class Cruiser implements Runnable {
            @Override
            public void run() {
                System.out.println("Run");
            }
        }

        Thread thread = new Thread(new Cruiser());
        thread.run(); //Run in current thread, test class
        thread.run(); //Run in current thread, test class
        thread.start(); // Create new thread
        //print Run Run Run
    }

    //Question 1
//    A) private synchronized SomeClass a; NO synchronized only methods or blocks
//    B) void book() { synchronized () {} } NO, no monitor object
//    C) public synchronized void book() {} OK, it's synchronized
//    D) public synchronized(this) void book() {} NO, this can not be used in synchronized block
//    E) public void book() { synchronized(Cruiser.class) {} } OK, synchronized by class
//    F) public void book() {synchronized(a){}} OK?, if a extended from Object
    //C, E, F?

    //Question 2
//  Run static method after sleep, called normally - "book" is printed
//    public class Hotel  {
//        private static void book() {
//            System.out.print("book");
//        }
//        public static void main(String[] args) {
//            Thread.sleep(1);
//            book();
//        }
//    }


}
