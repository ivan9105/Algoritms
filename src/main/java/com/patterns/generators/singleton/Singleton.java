package com.patterns.generators.singleton;

public class Singleton {
    private Singleton() {
    }

    private static class SingletonHolder {
        static final Singleton instance = new Singleton();

        //3
        static {
            System.out.println("Holder instance " + instance);
        }
    }

    //2
    public static Singleton getInstance() {
        System.out.println("Call get instance");
        return SingletonHolder.instance;
    }

    public static void main(String[] args) {
        //1
        System.out.println("Singleton example");
        Singleton.getInstance();
    }
}