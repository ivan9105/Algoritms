package com.leetcode.concurrent;

import java.util.function.IntConsumer;

import lombok.Getter;


public class FizzBuzzMultithreaded {
    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(15);
        Runnable printFizz = () -> System.out.print("Fizz, ");
        Runnable printBuzz = () -> System.out.print("Buzz, ");
        Runnable printFizzBuzz = () -> System.out.print("FizzBuzz, ");
        IntConsumer printInt = (int i) -> System.out.print(i + ",");

        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        fizzBuzz.fizz(printFizz);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            new Thread(() -> {
                while (true) {
                    try {
                        fizzBuzz.buzz(printBuzz);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                while (true) {
                    try {
                        fizzBuzz.fizzbuzz(printFizzBuzz);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                while (true) {
                    try {
                        fizzBuzz.number(printInt);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    /**
     * see PrintZeroEvenOddProblem, the same algorithm
     */
    @Getter
    static class FizzBuzz {
        private int n;
        private int cur = 1;
        private final Object mutex = new Object();

        public FizzBuzz(int n) {
            this.n = n;
        }

        // printFizz.run() outputs "fizz".
        public void fizz(Runnable printFizz) throws InterruptedException {
            synchronized (mutex) {
                while (cur <= n) {
                    if (cur % 3 == 0 && cur % 5 != 0) {
                        printFizz.run();
                        cur++;
                        mutex.notifyAll();
                    } else {
                        mutex.wait();
                    }
                }
            }
        }

        // printBuzz.run() outputs "buzz".
        public void buzz(Runnable printBuzz) throws InterruptedException {
            synchronized (mutex) {
                while (cur <= n) {
                    if (cur % 3 != 0 && cur % 5 == 0) {
                        printBuzz.run();
                        cur++;
                        mutex.notifyAll();
                    } else {
                        mutex.wait();
                    }
                }
            }
        }

        // printFizzBuzz.run() outputs "fizzbuzz".
        public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
            synchronized (mutex) {
                while (cur <= n) {
                    if (cur % 3 == 0 && cur % 5 == 0) {
                        printFizzBuzz.run();
                        cur++;
                        mutex.notifyAll();
                    } else {
                        mutex.wait();
                    }
                }
            }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void number(IntConsumer printNumber) throws InterruptedException {
            synchronized (mutex) {
                while (cur <= n) {
                    if (cur % 3 != 0 && cur % 5 != 0) {
                        printNumber.accept(cur);
                        cur++;
                        mutex.notifyAll();
                    } else {
                        mutex.wait();
                    }
                }
            }
        }
    }
}
