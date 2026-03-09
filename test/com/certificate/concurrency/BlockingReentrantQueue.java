package com.certificate.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingReentrantQueue<T> {
    private volatile int size = 0;
    private final Object[] content;
    private final int capacity;


    private final ReentrantLock lock = new ReentrantLock();
    private final Condition isEmptyCondition = lock.newCondition();
    private final Condition isFullCondition = lock.newCondition();

    BlockingReentrantQueue(int capacity) {
        try {
            lock.lock();
            this.capacity = capacity;
            this.content = new Object[capacity];
        } finally {
            lock.unlock();
        }
    }


    @SuppressWarnings("unchecked")
    T get() throws InterruptedException {
        try {
            lock.lockInterruptibly();
            if (size == 0) {
                while (size < 1) {
                    isEmptyCondition.await();
                }
            }
            final Object value = content[size - 1];
            content[--size] = null;
            isFullCondition.signal();
            return (T) value;
        } finally {
            lock.unlock();
        }
    }

    void put(T value) throws InterruptedException {
        try {
            lock.lockInterruptibly();
            if (size == capacity) {
                while (size == capacity) {
                    isFullCondition.await();
                }
            }
            content[++size] = value;
            isEmptyCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        //TODO
    }
}
