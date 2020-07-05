package com.core;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Хранит данные в порядке их добавления
 * если accessOrder = true элемент к которому обращаемся будет помещен в конец
 */
public class LinkedHashMapUsage {

    @Test
    public void accessOrderIsTrue() {
        Map<String, Integer> map = new LinkedHashMap<>(10, 0.75f, true);

        //TODO
    }
}
