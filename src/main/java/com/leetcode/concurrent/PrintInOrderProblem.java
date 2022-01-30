package com.leetcode.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * class Foo {
 * <p>
 * public Foo() {
 * <p>
 * }
 * <p>
 * public void first(Runnable printFirst) throws InterruptedException {
 * <p>
 * // printFirst.run() outputs "first". Do not change or remove this line.
 * printFirst.run();
 * }
 * <p>
 * public void second(Runnable printSecond) throws InterruptedException {
 * <p>
 * // printSecond.run() outputs "second". Do not change or remove this line.
 * printSecond.run();
 * }
 * <p>
 * public void third(Runnable printThird) throws InterruptedException {
 * <p>
 * // printThird.run() outputs "third". Do not change or remove this line.
 * printThird.run();
 * }
 * }
 */
//простое решение на замках, достаточно производительное
public class PrintInOrderProblem {

    private Runnable printFirst;
    private Runnable printSecond;
    private Runnable printThird;

    private CountDownLatch firstLatch = new CountDownLatch(1);
    private CountDownLatch secondLatch = new CountDownLatch(1);

    public static void main(String[] args) {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();

        firstLatch.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        firstLatch.await();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();

        secondLatch.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        firstLatch.await();
        secondLatch.await();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
