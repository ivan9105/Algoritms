package com.concurrent;

public class DoubleSynchronizationCase {

    synchronized public void m1() {
        System.out.println("Call m1");
    }

    synchronized public void m2() {
        m1();
        System.out.println("Call m2");
    }

    /**
     * Блокировки не будет
     * Так как Java проверяет что на этот уже удерживается блокировка этим же монитором (instance obj)
     */
    public static void main(String[] args) {
        new DoubleSynchronizationCase().m2();
        //System with exit code 0
    }
}
