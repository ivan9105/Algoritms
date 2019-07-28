package com.jmm;

public class WorkWithFinalFields {
    public static void main(String[] args) {
        //у нас есть объект с тяжелым конструктором
        //у нас есть singleton который его либо возвращает либо создает исп сихронизацию
        //и 2 потока которые его берут
        //нужно удостовериться что сушествует проблема вернуть частично недостроенный
        /**
         * private static Something instance = null;
         * public Something getInstance() {
         *     if (instance == null) {
         *         synchronized (this) {
         *             if (instance == null)
         *                 instance = new Something();
         *         }
         *     }
         *     return instance;
         * }
         */
        //потом пофиксим это final полями в самом объекте
        /**
         * private static class LazySomethingHolder {
         *     public static Something something = new Something();
         * }
         *
         * public static Something getInstance() {
         *     return LazySomethingHolder.something;
         * }
         */
    }
}
