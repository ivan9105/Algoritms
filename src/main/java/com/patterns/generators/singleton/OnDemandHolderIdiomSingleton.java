package com.patterns.generators.singleton;

public class OnDemandHolderIdiomSingleton {
    public OnDemandHolderIdiomSingleton() {
        System.out.println("Call constructor");
    }

    public static class OnDemandHolderIdiomSingletonHolder {
        public static final OnDemandHolderIdiomSingleton HOLDER_INSTANCE = new OnDemandHolderIdiomSingleton();
    }

    /**
     * поля inner static класса будут проинициализированы только при вызове переменной
     * потокобезопасность реализровано средствами JVM
     */
    public static OnDemandHolderIdiomSingleton getInstance() {
        return OnDemandHolderIdiomSingletonHolder.HOLDER_INSTANCE;
    }

    public static void main(String[] args) {
        System.out.println("Call getInstance");
        getInstance();
    }

    /**
     * 1) Использовать нормальную (не ленивую) инициализацию везде где это возможно;
     * 2) Для статических полей использовать On Demand Holder idiom;
     * 3) Для простых полей использовать Double Checked Lock & volatile idiom;
     * 4) Во всех остальных случаях использовать Synchronized accessor;
     */
}
