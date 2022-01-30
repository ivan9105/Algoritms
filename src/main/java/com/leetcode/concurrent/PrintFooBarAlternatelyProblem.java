package com.leetcode.concurrent;

import java.util.concurrent.Semaphore;

/**
 * class FooBar {
 * private int n;
 * <p>
 * public FooBar(int n) {
 * this.n = n;
 * }
 * <p>
 * public void foo(Runnable printFoo) throws InterruptedException {
 * <p>
 * for (int i = 0; i < n; i++) {
 * <p>
 * // printFoo.run() outputs "foo". Do not change or remove this line.
 * printFoo.run();
 * }
 * }
 * <p>
 * public void bar(Runnable printBar) throws InterruptedException {
 * <p>
 * for (int i = 0; i < n; i++) {
 * <p>
 * // printBar.run() outputs "bar". Do not change or remove this line.
 * printBar.run();
 * }
 * }
 * }
 */
public class PrintFooBarAlternatelyProblem {
    public static void main(String[] args) {

    }

    class FooBar {
        private Runnable printFoo;
        private Runnable printBar;

        private Semaphore fooSemaphore = new Semaphore(0);
        private Semaphore barSemaphore = new Semaphore(1);

        private int n;

        public FooBar(int n) {
            this.n = n;
        }

        public void foo(Runnable printFoo) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                //счетчик == 1 пускаем выполнение print
                barSemaphore.acquire();
                printFoo.run();
                //счетчик после release становить 1
                fooSemaphore.release();
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                //изначально permits == 0 соответственно поток который вызвает поток ждет
                fooSemaphore.acquire();
                printBar.run();
                //счетчик после release становиться 1
                barSemaphore.release();
            }
        }
    }
}
