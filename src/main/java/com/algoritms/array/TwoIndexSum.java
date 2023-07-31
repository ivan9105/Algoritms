package com.algoritms.array;

import java.util.HashMap;

import org.apache.commons.math3.util.Pair;

public class TwoIndexSum {

    // Даны список чисел и целевая сумма.
    // Вернуть _индексы_ двух элементов списка, сумма которых равна целевой.
    // Можно считать, что такая пара слагаемых заведомо существует в списке.
    // from typing import List, Tuple
    // input_list = [2,4, 7, 5, 2]
    // target = 9
    public static void main(String[] args) {
        var arr = new int[]{2, 4, 7, 5, 2};
        var result = calculate(arr, 9);
        System.out.println("First index: " + result.getFirst() + ", second index: " + result.getSecond());
    }

    private static Pair<Integer, Integer> calculate(int[] arr, int target) {
        var invertArrayMap = new HashMap<Integer, Integer>();
        for (int index = 0; index < arr.length; index++) {
            var secondValue = target - arr[index];
            if (invertArrayMap.containsKey(secondValue)) {
                return new Pair<>(index, invertArrayMap.get(secondValue));
            } else {
                invertArrayMap.put(secondValue, index);
            }
        }
        return new Pair<>(-1, -1);
    }
}
