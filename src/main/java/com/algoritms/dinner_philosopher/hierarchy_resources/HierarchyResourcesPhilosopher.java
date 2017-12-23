package com.algoritms.dinner_philosopher.hierarchy_resources;

import com.algoritms.dinner_philosopher.Fork;
import com.algoritms.dinner_philosopher.Philosopher;

public class HierarchyResourcesPhilosopher extends Philosopher {
    private boolean correctOrder;

    public HierarchyResourcesPhilosopher(String philosopherName, Fork leftFork, Fork rightFork) {
        super(philosopherName, leftFork, rightFork);
        int rightOrder = ((HierarchyResourcesFork) this.rightFork).getOrder();
        int leftOrder = ((HierarchyResourcesFork) this.leftFork).getOrder();
        this.correctOrder = rightOrder < leftOrder;
    }

    @Override
    public void run() {
        try {
            if (correctOrder) {
                while (true) {
                    think();
                    synchronized (leftFork) {
                        getLeftFork();
                        synchronized (rightFork) {
                            getRightFork();
                            eat();
                            putLeftFork();
                        }
                        putRightFork();
                    }
                }
            } else {
                while (true) {
                    think();
                    synchronized (rightFork) {
                        getRightFork();
                        synchronized (leftFork) {
                            getLeftFork();
                            eat();
                            putRightFork();
                        }
                        putLeftFork();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
