package com.algoritms.dinner_philosopher.monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;
import static java.lang.System.nanoTime;

public class Monitor {
    private Lock mutex = new ReentrantLock();
    private Condition[] conditions;
    private int size;

    public Monitor(int size) {
        this.size = size;
        this.conditions = new Condition[size];

        for (int i = 0; i < size; i++) {
            conditions[i] = mutex.newCondition();
        }
    }

    private void getLeftFork(int id, MonitorFork leftFork) {
        System.out.println(format("Philosoper[%s]: get left fork '%s', time '%s'", id, leftFork.toString(), nanoTime()));
    }

    private void getRightFork(int id, MonitorFork rightFork) {
        System.out.println(format("Philosoper[%s]: get right fork '%s', time '%s'", id, rightFork.toString(), nanoTime()));
    }

    private void putLeftFork(int id, MonitorFork leftFork) {
        System.out.println(format("Philosoper[%s]: put left fork '%s', time '%s'", id, leftFork.toString(), nanoTime()));
    }

    private void putRightFork(int id, MonitorFork rightFork) {
        System.out.println(format("Philosoper[%s]: put right fork '%s', time '%s'", id, rightFork.toString(), nanoTime()));
    }

    public void getForks(int id, MonitorFork leftFork, MonitorFork rightFork) {
        mutex.lock();
        try {
            while (!leftFork.isAvailability() || !rightFork.isAvailability()) {
                conditions[id].await();
            }
            leftFork.setAvailability(false);
            rightFork.setAvailability(false);
            getLeftFork(id, leftFork);
            getRightFork(id, rightFork);
        } catch (Exception ignore) {
        } finally {
            mutex.unlock();
        }
    }

    public void putForks(int id, MonitorFork leftFork, MonitorFork rightFork) {
        mutex.lock();
        try {
            leftFork.setAvailability(true);
            rightFork.setAvailability(true);
            //next prev id signal all
            conditions[(id + 1) % size].signalAll();
            conditions[(id + size - 1) % size].signalAll();
            putLeftFork(id, leftFork);
            putRightFork(id, rightFork);
        } finally {
            mutex.unlock();
        }
    }
}
