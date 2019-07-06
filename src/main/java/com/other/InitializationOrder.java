package com.other;

/**
 Статический блок родителя
 Статические блок наследника
 Статические переменные родителя
 Нестатический блок родителя
 Конструктор родителя
 Статические переменные наследника
 Нестатический блок наследника
 Конструктор наследника
 */
public class InitializationOrder {

    public static void main(String[] args) {
        InitializationOrder executor = new InitializationOrder();
        executor.execute();
    }

    public void execute() {
        new Child();
    }
}

class Parent {
    private static int x;
    private static int y;

    static {
        x = 10;
        System.out.println("Parent static block x = " + x + ", y = " + y);
    }

    {
        System.out.println("Parent not static block, z = " + z);
    }

    private static int z = 20;

    public Parent() {
        y = 30;
        System.out.println("Parent constructor x = " + x + ", y = " + y + ", z = " + z);
    }
}

class Child extends Parent {
    private static int x1;
    private static int y1;

    static {
        x1 = 10;
        System.out.println("Child static block x = " + x1 + ", y = " + y1);
    }

    {
        System.out.println("Child not static block, z = " + z1);
    }

    private static int z1 = 20;

    public Child() {
        y1 = 30;
        System.out.println("Child constructor x = " + x1 + ", y = " + y1 + ", z = " + z1);
    }
}