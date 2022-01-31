package com.leetcode.concurrent;

public class DiningPhilosophersProblem {
    public static void main(String[] args) {

    }

    /**
     * См реализации com.algoritms.dinner_philosopher
     * Leetcode это дичь схвал сихронизованный блок
     * для галочки делаю коммит
     */
    class DiningPhilosophers {

        public DiningPhilosophers() {
        }

        // call the run() method of any runnable to execute its code
        public void wantsToEat(int philosopher,
                               Runnable pickLeftFork,
                               Runnable pickRightFork,
                               Runnable eat,
                               Runnable putLeftFork,
                               Runnable putRightFork) throws InterruptedException {

            synchronized(this){
                pickLeftFork.run();
                pickRightFork.run();
                eat.run();
                putLeftFork.run();
                putRightFork.run();
            }
        }
    }
}
