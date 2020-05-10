package com.patterns.generators.prototype;

public class PrototypeUsage {
    public static void main(String[] args) {
        Shape shape = new Rectangle(0, 0, "#333", 100, 100);
        Shape prototype = shape.prototype();
        System.out.println(shape.hashCode() == prototype.hashCode() && shape.equals(prototype) && shape != prototype);
    }

}
