package com.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

public class GenericsUsage {

    @Test
    public void wildcard() {
        /**
         * Generic отрабатывают в момент компиляции
         * wildcard - ? - любой тип
         */
        printCollection(newArrayList(new Object(), new Object()));
        printCollection(newArrayList("1", 2));
    }

    @Test
    public void boundedWildcard() {
        /**
         * bounded wildcard - ? extends любой тип
         */
        printShapeCollection(newArrayList(new Square(), new Circle()));
    }

    @Test
    public void genericMethod() {
        addArrayToCollection(new String[]{"1"}, new ArrayList<String>());
    }

    @Test
    public void genericClass() {
        /**
         * Наследование в generic можно исп через &
         * например ? extends InterfaceOne & InterfaceTwo
         */
    }

    @Test
    public void boundedTypeArgument() {
        /**
         * <M, N extends M> void addAll(Collection<N> c, Collection<M> c2)
         */
    }

    @Test
    public void lowerBoundedWildcard() {
        /**
         * <T extends Comparable<T>> T max(Collection<T> c)
         */
    }

    @Test
    public void wildcardCapture() {
        /**
         * <T> void swapImpl(List<T> list, int i, int j)
         */
    }

    @Test
    public void genericRestrictions() {
        /**
         * невозможно создать массив generic-ов
         * new T[10]; //error
         * new List<?>[10] //error
         */
    }

    @Test
    public void superGenerics() {
        /**
         * Predicate<? super V> p
         * предикат от V или любого супертипа V (вплоть до Object)
         */
    }

    private void printCollection(Collection<?> anyTypeCollection) {
        for (Object next : anyTypeCollection) {
            System.out.println(next);
        }
    }

    private void printShapeCollection(Collection<? extends Shape> shapeTypeCollection) {
        for (Object next : shapeTypeCollection) {
            System.out.println(next);
        }
    }

    private <T> void addArrayToCollection(T[] array, Collection<T> collection) {
        collection.addAll(asList(array));
    }

    private abstract static class Shape {
    }

    private static class Circle extends Shape {
    }

    private static class Square extends Shape {
    }
}
