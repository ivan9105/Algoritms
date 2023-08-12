package com.concurrent;

public class DoubleStaticSynchronizationCase {

    synchronized public static void m1() {
        System.out.println("Call m1");
    }

    synchronized public static void m2() {
        m1();
        System.out.println("Call m2");
    }

    /**
     * Блокировки не будет
     * Так как Java проверяет что на этот уже удерживается блокировка этим же монитором (.class)
     */
    public static void main(String[] args) {
        DoubleStaticSynchronizationCase.m2();
        //System with exit code 0
    }
}
