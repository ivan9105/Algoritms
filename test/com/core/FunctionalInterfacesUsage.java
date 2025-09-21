package com.core;

import org.junit.Test;

import java.util.Random;
import java.util.function.*;

public class FunctionalInterfacesUsage {

    @Test
    public void customFunctionalInterface() {
        IntegerPredicate moreThanZero = x -> x > 0;
        System.out.println(moreThanZero.test(5));
    }

    @Test
    public void function() {
        /**
         * два п-ра:
         * первый - аргумент
         * второй - возвращаемое значение
         */

        System.out.println(new Incrementer().apply(1L));
    }

    @Test
    public void predicate() {
        /**
         * Простая функция на вход Any на выход boolean значение
         */
        System.out.println(new CheckIsNull().test(null));
    }

    @Test
    public void unaryOperator() {
        /**
         * Простая фунция на вход type T на выход тоже type T
         */

        System.out.println(new UnaryIncrementer().apply(1));
    }

    @Test
    public void binaryOperator() {
        /**
         * Простая ф-я на вход 2 параметра одинокового типа на выход значения того же типа
         */

        System.out.println(new SumOperator().apply(1, 1));
    }

    @Test
    public void supplier() {
        /**
         * Supplier - фабрика
         */
        System.out.println(new RandomInt().get());
    }

    @Test
    public void consumer() {
        /**
         * Ф-я которая принимает на вход любой аргумент
         * и обрабатывает его
         */
        new Processor().accept(3);
    }
}

/**
 * Основное назначение – использование в лямбда выражениях и method reference.
 * Наличие 1 абстрактного метода - это единственное условие,
 * таким образом функциональный интерфейс может содержать так же default и static методы.
 */
@FunctionalInterface
interface IntegerPredicate {
    boolean test(Integer value);
}

class Incrementer implements Function<Long, Long> {

    @Override
    public Long apply(Long value) {
        return ++value;
    }
}

class CheckIsNull implements Predicate<Object> {

    @Override
    public boolean test(Object object) {
        return object != null;
    }
}

class UnaryIncrementer implements UnaryOperator<Integer> {

    @Override
    public Integer apply(Integer integer) {
        return ++integer;
    }
}

class SumOperator implements BinaryOperator<Integer> {

    @Override
    public Integer apply(Integer first, Integer second) {
        return first + second;
    }
}

class RandomInt implements Supplier<Integer> {
    private Random random = new Random();

    @Override
    public Integer get() {
        return random.nextInt();
    }
}

class Processor implements Consumer<Integer> {

    @Override
    public void accept(Integer integer) {
        System.out.println("Do process...");
    }
}