package com.algoritms.dinner_philosopher.waiter;

import com.algoritms.dinner_philosopher.Fork;

import java.util.List;

public class Waiter {
    private List<WaiterPhilosopher> philosophers;

    public Waiter(List<WaiterPhilosopher> philosophers) {
        this.philosophers = philosophers;
        this.philosophers.forEach(philosopher -> philosopher.setWaiter(this));
    }

    /**
     * check free fork's state
     *
     * @param left {@link Fork}
     * @return boolean value, true if free
     */
    private boolean checkFork(WaiterPhilosopher philosopher, boolean left) {
        try {
            int currentIndex = philosophers.indexOf(philosopher);
            if (left) {
                int prevIndex = currentIndex == 0 ? philosophers.size() - 1 : currentIndex - 1;
                WaiterPhilosopher prevPhilosopher = philosophers.get(prevIndex);
                return !prevPhilosopher.isLeftState() && !prevPhilosopher.isRightState();
            } else {
                int nextIndex = currentIndex == philosophers.size() - 1 ? 0 : currentIndex + 1;
                WaiterPhilosopher nextPhilosopher = philosophers.get(nextIndex);
                return !nextPhilosopher.isLeftState() && !nextPhilosopher.isRightState();
            }
        } catch (Exception e) {
            System.exit(-1);
            return false;
        }
    }

    public synchronized int getLeftFork(WaiterPhilosopher philosopher) throws InterruptedException {
        if (!checkFork(philosopher, true)) {
            return -1;
        } else {
            philosopher.setLeftState(true);
            return philosopher.getLeftFork();
        }
    }

    public synchronized int getRightFork(WaiterPhilosopher philosopher) throws InterruptedException {
        if (!checkFork(philosopher, false)) {
            return -1;
        } else {
            philosopher.setRightState(true);
            return philosopher.getRightFork();
        }
    }

    public synchronized void putLeftFork(WaiterPhilosopher philosopher) throws InterruptedException {
        philosopher.putLeftFork();
        philosopher.setLeftState(false);
    }

    public synchronized void putRightFork(WaiterPhilosopher philosopher) throws InterruptedException {
        philosopher.putRightFork();
        philosopher.setRightState(false);
    }
}
