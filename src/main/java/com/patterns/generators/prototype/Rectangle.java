package com.patterns.generators.prototype;

import static java.util.Objects.hash;

public class Rectangle extends Shape {
    private int width;
    private int height;

    public Rectangle(int x, int y, String color, int width, int height) {
        super(x, y, color);
        this.width = width;
        this.height = height;
    }

    //можно заменить методом clone
    @Override
    public Rectangle prototype() {
        return new Rectangle(this.x, this.y, this.color, this.width, this.height);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        boolean instanceOf = object instanceof Rectangle;
        if (!instanceOf) {
            return false;
        }

        Rectangle other = (Rectangle) object;

        return this.x == other.x &&
                this.y == other.y &&
                this.height == other.height &&
                this.width == other.width &&
                (this.color == null && other.color == null) ||
                (other.color != null && this.color != null && this.color.equals(other.color));
    }

    @Override
    public int hashCode() {
        return hash(x, y, color, width, height);
    }
}
