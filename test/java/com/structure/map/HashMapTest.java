package com.structure.map;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class HashMapTest {

    //TODO parallel stream exception handling

    @Test
    public void treeNodeFindWithBadKey() {
        Map<KeyWithBadHashCodeAndEquals, Integer> map = new HashMap<>();

        map.put(new KeyWithBadHashCodeAndEquals(10, false), 0);
        KeyWithBadHashCodeAndEquals targetKey = new KeyWithBadHashCodeAndEquals(10, false);
        map.put(targetKey, 1);

        /**
         * надо запомнить зависимость hashcode объекта/размер массива/load factor
         * в одном бакете могут быть элементы с разными хешами и тогда дерево полезно
         * логарифмическая сложность выигрывает
         */
        IntStream.range(2, 100).forEach(it -> map.put(new KeyWithBadHashCodeAndEquals(10, false), it));

        Integer integer = map.get(targetKey);
        /**
         * Поиск по дереву ничего не будет возвращать в данном случае
         * Вернется значение на проверку по сравнению ссылок на объект
         * мы n раз пройдемся пока есть next у treeNode
         * treeNode в таком случае не будет делать полный обход
         * и будет возвращать null на данный случай
         * и так до тех пока reference key object не воспадет
         */
        assertEquals(1, integer.intValue());
    }

    private static class KeyWithBadHashCodeAndEquals {
        private int hashcode;
        private boolean equals;

        public KeyWithBadHashCodeAndEquals(int hashCode, boolean equals) {
            this.hashcode = hashCode;
            this.equals = equals;
        }

        @Override
        public boolean equals(Object o) {
            return equals;
        }

        @Override
        public int hashCode() {
            return hashcode;
        }
    }
}
