package com.patterns.structural.decorator;

public class DecoratorUsage {
    /**
     * Добавляет родственным объектам дополнительную функциональность оборачивая в полезные обертки
     */
    public static void main(String[] args) {
        DecoratorUsage executor = new DecoratorUsage();
        executor.execute();
    }

    public void execute() {
        new NegativeEffectDrawer(
                new BorderDrawer(
                        new PatternBorderDrawer(
                                new ColorDrawer(
                                        new ShapeDrawer()
                                )
                        )
                )
        ).draw();
    }
}

interface Drawer {
    void draw();
}

abstract class AbstractDecorateDrawer implements Drawer {
    private Drawer drawer;

    public AbstractDecorateDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }
}

class ShapeDrawer implements Drawer {
    @Override
    public void draw() {
        drawShape();
    }

    private void drawShape() {
        System.out.println("Draw shape");
    }
}

class ColorDrawer extends AbstractDecorateDrawer {
    public ColorDrawer(Drawer drawer) {
        super(drawer);
    }

    @Override
    public void draw() {
        getDrawer().draw();
        fillColor();
    }

    private void fillColor() {
        System.out.println("Fill by color");
    }
}

class BorderDrawer extends AbstractDecorateDrawer {

    public BorderDrawer(Drawer drawer) {
        super(drawer);
    }

    @Override
    public void draw() {
        getDrawer().draw();
        makeBorder();
    }

    private void makeBorder() {
        System.out.println("Make border");
    }
}

class PatternBorderDrawer extends AbstractDecorateDrawer {

    public PatternBorderDrawer(Drawer drawer) {
        super(drawer);
    }

    @Override
    public void draw() {
        getDrawer().draw();
        makePattern();
    }

    private void makePattern() {
        System.out.println("Make pattern");
    }
}

class NegativeEffectDrawer extends AbstractDecorateDrawer {

    public NegativeEffectDrawer(Drawer drawer) {
        super(drawer);
    }

    @Override
    public void draw() {
        getDrawer().draw();
        applyNegativeEffect();
    }

    private void applyNegativeEffect() {
        System.out.println("Apply negative effect");
    }
}
