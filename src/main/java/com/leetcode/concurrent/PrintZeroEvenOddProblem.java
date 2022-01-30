package com.leetcode.concurrent;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * class ZeroEvenOdd {
 * private int n;
 * <p>
 * public ZeroEvenOdd(int n) {
 * this.n = n;
 * }
 * <p>
 * // printNumber.accept(x) outputs "x", where x is an integer.
 * public void zero(IntConsumer printNumber) throws InterruptedException {
 * <p>
 * }
 * <p>
 * public void even(IntConsumer printNumber) throws InterruptedException {
 * <p>
 * }
 * <p>
 * public void odd(IntConsumer printNumber) throws InterruptedException {
 * <p>
 * }
 * }
 */
public class PrintZeroEvenOddProblem {
    public static void main(String[] args) {

    }

    class ZeroEvenOdd {
        private int n;
        //альтернатива 2 монитора + флажки + крит секция
        //первично блокируем print even/odd, разрешаем только zero
        private Semaphore zeroSemaphore = new Semaphore(1);
        private Semaphore evenSemaphore = new Semaphore(0);
        private Semaphore oddSemaphore = new Semaphore(0);

        public ZeroEvenOdd(int n) {
            this.n = n;
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        //синхронизация на уровне
        public void zero(IntConsumer printNumber) throws InterruptedException {
            for (int i = 1; i <= n; i++) {
                //zero permit = 0
                zeroSemaphore.acquire();

                printNumber.accept(0);
                //в зависимости от текущего числа разрешаем либо even/либо odd
                if (i % 2 == 0) {
                    evenSemaphore.release();
                } else {
                    oddSemaphore.release();
                }

            }
        }

        //четные
        public void even(IntConsumer printNumber) throws InterruptedException {
            for (int i = 2; i <= n; i += 2) {
                evenSemaphore.acquire();
                printNumber.accept(i);
                zeroSemaphore.release();
                //возврат к точке синхронизации zero
            }
        }

        //нечетные
        public void odd(IntConsumer printNumber) throws InterruptedException {
            for (int i = 1; i <= n; i += 2) {
                oddSemaphore.acquire();
                printNumber.accept(i);
                zeroSemaphore.release();
                //возврат к точке синхронизации zero
            }
        }
    }
}
