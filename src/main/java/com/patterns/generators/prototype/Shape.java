package com.patterns.generators.prototype;

public abstract class Shape {
    protected int x;
    protected int y;
    protected String color;

    private Shape() {
    }

    public Shape(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public abstract Shape prototype();
}
