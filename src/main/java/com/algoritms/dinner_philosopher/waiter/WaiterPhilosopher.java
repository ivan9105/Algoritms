package com.algoritms.dinner_philosopher.waiter;

import com.algoritms.dinner_philosopher.Fork;
import com.algoritms.dinner_philosopher.Philosopher;


public class WaiterPhilosopher extends Philosopher {
    private Waiter waiter;
    private boolean leftState = false;
    private boolean rightState = false;

    public WaiterPhilosopher(String philosopherName, Fork leftFork, Fork rightFork) {
        super(philosopherName, leftFork, rightFork);
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                while (waiter.getLeftFork(this) == -1) {
                }
                while (waiter.getRightFork(this) == -1) {
                }
                eat();
                waiter.putRightFork(this);
                waiter.putLeftFork(this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public boolean isLeftState() {
        return leftState;
    }

    public void setLeftState(boolean leftState) {
        this.leftState = leftState;
    }

    public boolean isRightState() {
        return rightState;
    }

    public void setRightState(boolean rightState) {
        this.rightState = rightState;
    }
}
