package com.patterns.generators.singleton;

public class DoubleCheckingLockingAndVolatileSingleton {
    private static volatile DoubleCheckingLockingAndVolatileSingleton instance;

    /**
     * Так как у нас метод не synchronized
     * сюда может залететь любое кол-во потоков в еденицу времени
     * первый check через локальную переменную до synchronized блока чтобы проверить что другой поток не начал инициализацию класса
     * второй check ну чисто в теории есть вероятность что все таки другой поток успел залететь в synchronized одновременно с первым
     */

    /**
     * БОЛЕЕ правильное описание
     * без volatile работать будет но создатели jdk рекомендуют исп volatile
     * когда мы пишем new LazySynchronizedBlockSingleton()
     * 1) local_ptr = malloc(sizeof(LazySynchronizedBlockSingleton)) // выделение памяти под сам объект;
     * 2) s = local_ptr // инициализация указателя;
     * 3) LazySynchronizedBlockSingleton::ctor(s); // конструирование объекта (инициализация полей);
     */
    public static DoubleCheckingLockingAndVolatileSingleton getInstance() {
        DoubleCheckingLockingAndVolatileSingleton localInstance = instance;
        if (localInstance == null) {
            synchronized (DoubleCheckingLockingAndVolatileSingleton.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DoubleCheckingLockingAndVolatileSingleton();
                }
            }
        }
        return localInstance;
    }
}
