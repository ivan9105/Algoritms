package com.jmm.virtual_function;

public class Child extends Parent {
    String id = "Child";
    static int age = 5;

    String getId() {
        return id;
    }

    static Integer getAge() {
        return age;
    }
}
