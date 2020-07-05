package com.algoritms.dinner_philosopher;

import static java.lang.String.format;
import static java.lang.System.nanoTime;

public class Philosopher extends Thread {
    protected static final int RANDOM_FACTOR = 10;
    private String philosopherName;
    protected final Fork leftFork;
    protected final Fork rightFork;

    public Philosopher(String philosopherName, Fork leftFork, Fork rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.philosopherName = philosopherName;
    }

    //default implementation catch deadlock
    @Override
    public void run() {
        try {
            philosopherProcess();
        } catch (InterruptedException ignore) {
        }
    }

    protected void philosopherProcess() throws InterruptedException {
        while (true) {
            think();
            synchronized (leftFork) {
                getLeftFork();
                synchronized (rightFork) {
                    getRightFork();
                    eat();
                    putRightFork();
                }
                putLeftFork();
            }
        }
    }

    protected void eat() throws InterruptedException {
        System.out.println(format("%s: to eat, time '%s'", toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    protected void think() throws InterruptedException {
        System.out.println(format("%s: to think, time '%s'", toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    public int getLeftFork() throws InterruptedException {
        System.out.println(format("%s: get left fork '%s', time '%s'", toString(), leftFork.toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
        return 1;
    }

    public int getRightFork() throws InterruptedException {
        System.out.println(format("%s: get right fork '%s', time '%s'", toString(), rightFork.toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
        return 1;
    }

    public void putRightFork() throws InterruptedException {
        System.out.println(format("%s: put right fork '%s', time '%s'", toString(), leftFork.toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    public void putLeftFork() throws InterruptedException {
        System.out.println(format("%s: put left fork '%s', time '%s'", toString(), leftFork.toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    public String getPhilosopherName() {
        return philosopherName;
    }

    public void setPhilosopherName(String philosopherName) {
        this.philosopherName = philosopherName;
    }

    @Override
    public String toString() {
        return philosopherName;
    }
}
