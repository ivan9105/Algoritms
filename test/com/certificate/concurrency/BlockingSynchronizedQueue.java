package com.certificate.concurrency;

public class BlockingSynchronizedQueue<T> {
    private volatile int size = 0;
    private final Object[] content;
    private final int capacity;

    private final Object isEmptyMonitor = new Object();
    private final Object isFullMonitor = new Object();

    BlockingSynchronizedQueue(final int capacity) {
        this.capacity = capacity;
        this.content = new Object[this.capacity];
    }

    @SuppressWarnings("unchecked")
    T get() throws InterruptedException {
        if (size == 0) {
            synchronized (isEmptyMonitor) {
                while (size < 1) {
                    isEmptyMonitor.wait();
                }
            }
        }
        try {
            synchronized (this) {
                final Object value = content[size - 1];
                content[--size] = null;
                return (T) value;
            }
        } finally {
            synchronized (isFullMonitor) {
                isFullMonitor.notify();
            }
        }
    }

    void put(T value) throws InterruptedException {
        if (size == capacity) {
            synchronized (isFullMonitor) {
                while (size == capacity) {
                    isFullMonitor.wait();
                }
            }
        }
        synchronized (this) {
            content[++size] = value;
        }
        synchronized (isEmptyMonitor) {
            isEmptyMonitor.notify();
        }
    }

    public static void main(String[] args) {
        //TODO
    }
}
