package com.jmm.virtual_function;

public class VirtualFunctionUsage {
    public static void main(String[] args) {
        Parent parent = new Child();

        /**
         * Поля
         * Статические методы/поля
         * Конструкторы
         * не наследуются
         *
         * Поэтому в данном случае id значение берется из Parent класса
         * статическое связывание
         */
        //Parent
        System.out.println(parent.id);

        /**
         * Конкретно тут мы имеем дело с динамическим связыванием
         * При вызове метода getId() мы обращаемся к виртуальной таблице класса Child
         * там метод getId() у Parent не является final
         */
//        Child
        System.out.println(parent.getId());

        //true
        System.out.println(parent.getAge() == parent.age);
    }
}
