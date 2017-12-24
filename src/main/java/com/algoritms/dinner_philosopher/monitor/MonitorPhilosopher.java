package com.algoritms.dinner_philosopher.monitor;

import static java.lang.String.format;
import static java.lang.System.nanoTime;

public class MonitorPhilosopher implements Runnable {
    private static final int RANDOM_FACTOR = 10;

    private int id;
    private Monitor monitor;
    private MonitorFork leftFork;
    private MonitorFork rightFork;

    public MonitorPhilosopher(int id, MonitorFork leftFork, MonitorFork rightFork, Monitor monitor) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                getForks();
                eat();
                putForks();
            }
        } catch (InterruptedException ignore) {
        }
    }

    private void putForks() throws InterruptedException {
        monitor.putForks(getId(), leftFork, rightFork);
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    private void getForks() throws InterruptedException {
        monitor.getForks(getId(), leftFork, rightFork);
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    protected void eat() throws InterruptedException {
        System.out.println(format("%s: to eat, time '%s'", toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    protected void think() throws InterruptedException {
        System.out.println(format("%s: to think, time '%s'", toString(), nanoTime()));
        Thread.sleep(((int) (Math.random() * RANDOM_FACTOR)));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public MonitorFork getLeftFork() {
        return leftFork;
    }

    public void setLeftFork(MonitorFork leftFork) {
        this.leftFork = leftFork;
    }

    public MonitorFork getRightFork() {
        return rightFork;
    }

    public void setRightFork(MonitorFork rightFork) {
        this.rightFork = rightFork;
    }

    @Override
    public String toString() {
        return format("Philosopher[%s]", id);
    }
}
