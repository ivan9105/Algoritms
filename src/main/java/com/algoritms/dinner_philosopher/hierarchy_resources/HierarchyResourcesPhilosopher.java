package com.algoritms.dinner_philosopher.hierarchy_resources;

import com.algoritms.dinner_philosopher.Philosopher;

public class HierarchyResourcesPhilosopher extends Philosopher {
    private final boolean isCorrectOrder;

    public HierarchyResourcesPhilosopher(
            String philosopherName,
            HierarchyResourcesFork leftFork,
            HierarchyResourcesFork rightFork
    ) {
        super(philosopherName, leftFork, rightFork);

        int rightOrder = rightFork.getOrder();
        int leftOrder = leftFork.getOrder();
        this.isCorrectOrder = rightOrder < leftOrder;
    }

    @Override
    public void run() {
        try {
            if (isCorrectOrder) {
                philosopherProcess();
            } else {
                philosopherProcessInIncorrectOrder();
            }
        } catch (InterruptedException ignore) {
        }
    }

    private void philosopherProcessInIncorrectOrder() throws InterruptedException {
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

    @Override
    protected void philosopherProcess() throws InterruptedException {
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
    }
}
