package com.patterns.generators.singleton;

public class SynchronizedAccessorSingleton {
    private static SynchronizedAccessorSingleton instance;

    /**
     * тут все понятно
     * только один поток может зайти в критическую секцию
     * синхронизация на уровне класса гарантирует нам что вызов может быть только 1 через 1 thread в 1 единицу времени
     *
     * synchronized - низкая производительность
     */
    public static synchronized SynchronizedAccessorSingleton getInstance() {
        if (instance == null) {
            instance = new SynchronizedAccessorSingleton();
        }
        return instance;
    }
}
