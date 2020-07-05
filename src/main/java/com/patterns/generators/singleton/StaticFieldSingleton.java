package com.patterns.generators.singleton;

public class StaticFieldSingleton {
    /**
     * Простая и прозрачная реализация
     * Потокобезопасность
     *
     * Статическое поле будет проинициализировано в момент компиляции
     * собственно за счет этого мы потокобезопасные
     */
    public static final StaticFieldSingleton INSTANCE = new StaticFieldSingleton();

    public static void main(String[] args) {
        StaticFieldSingleton instance = INSTANCE;
    }
}
