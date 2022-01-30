package com.leetcode.concurrent;

/**
 * class H2O {
 *
 *     public H2O() {
 *
 *     }
 *
 *     public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
 *
 *         // releaseHydrogen.run() outputs "H". Do not change or remove this line.
 *         releaseHydrogen.run();
 *     }
 *
 *     public void oxygen(Runnable releaseOxygen) throws InterruptedException {
 *
 *         // releaseOxygen.run() outputs "O". Do not change or remove this line.
 * 		releaseOxygen.run();
 *     }
 * }
 *
 * На вход у нас массив элементов состоящие из водорода и кислорода в неявном виде
 * нам необходимо наладить выпуск "Воды"
 * формула воды H20 == HH0
 * не важно какая последовательность "выпуска" H или 0
 * нам важно соблюсти неделимый порядок операций 2H + 0
 *
 * Example 1:
 *
 * Input: water = "HOH"
 * Output: "HHO"
 * Explanation: "HOH" and "OHH" are also valid answers.
 * Example 2:
 *
 * Input: water = "OOHHHH"
 * Output: "HHOHHO"
 * Explanation: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" and "OHHOHH" are also valid answers.
 */
public class BuildingH20Problem {
    public static void main(String[] args) {

    }

    class H2O {

        private int countOfHydrogen = 0;
        private int countOfOxygen = 0;
        private final Object mutex = new Object();

        public H2O() {
        }

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
            synchronized (mutex) {
                while (countOfHydrogen == 2)  {
                    mutex.wait();
                }

                countOfHydrogen++;

                // releaseHydrogen.run() outputs "H". Do not change or remove this line.
                releaseHydrogen.run();

                if (countOfHydrogen == 2 && countOfOxygen == 1) {
                    countOfHydrogen = 0;
                    countOfOxygen = 0;
                }

                mutex.notifyAll();
            }
        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
            synchronized (mutex) {
                while (countOfOxygen == 1)  {
                    mutex.wait();
                }

                countOfOxygen++;

                // releaseOxygen.run() outputs "O". Do not change or remove this line.
                releaseOxygen.run();

                if (countOfHydrogen == 2 && countOfOxygen == 1) {
                    countOfHydrogen = 0;
                    countOfOxygen = 0;
                }

                mutex.notifyAll();
            }
        }
    }
}
