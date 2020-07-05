package com.solid;

import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

public class OpenCloseExample {
    /**
     * суть принципа
     * позволить расширять поведение классов
     * без изменения базового класса
     *
     * другими словами систему должна быть гибкой
     * не должно быть гигансткой заплатки в одном методе принимающего базовый класс с кучей проверок instanceOf/switch - type
     *
     * в данном примере мы инкапсулировали логику в самом дто
     *
     * в случае исп-я в enterprise я бы заложился на поле type и сделал бы мэппинг на отдельные компоненты
     * и вместо ShapeAreaCalculator который вызывал не Shape#calculateArea, вызывался бы ConcreteShapeCalculator
     * и это бы соотвествовало бы данному принципу
     */
    public static void main(String[] args) {
        OpenCloseExample executor = new OpenCloseExample();
        executor.execute();
    }

    private void execute() {
        new ShapeAreaCalculator()
                .sum(
                        ImmutableSet.of(
                                Square.builder()
                                        .size(4.4d)
                                        .build(),
                                Circle.builder()
                                        .radius(5.4d)
                                        .build(),
                                Rectangle.builder()
                                        .height(33.4d)
                                        .width(343.d)
                                        .build()
                        )
                );
    }
}

/**
 * Принцип открытости закрытости заключается в том что мы инкапсулируем расчет площади в конкретном экзепляре элемента
 */
class ShapeAreaCalculator {
    Double sum(Collection<Shape> shapes) {
        return shapes.stream()
                .map(Shape::calculateArea)
                .reduce(Double::sum)
                .orElseThrow(RuntimeException::new);
    }
}

interface Shape {
    Double calculateArea();
}

@Data
@Builder
class Circle implements Shape {
    private Double radius;

    @Override
    public Double calculateArea() {
        return (22 / 7) * radius * radius;
    }
}

@Data
@Builder
class Square implements Shape {
    private Double size;

    @Override
    public Double calculateArea() {
        return size * size;
    }
}

@Data
@Builder
class Rectangle implements Shape {
    private Double height;
    private Double width;

    @Override
    public Double calculateArea() {
        return height * width;
    }
}
