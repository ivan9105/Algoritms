package com.algoritms.dynamic;

import java.util.Arrays;

/**
 * Ребенок поднимается по лестнице из n ступенек. За один шаг он может переместиться
 * на одну, две или три ступеньки. Реализуйте метод, рассчитывающий
 * количество возможных вариантов перемещения ребенка по лестнице.
 * <p>
 * СТР 356
 * <p>
 * MAX 3 ступеньки
 */
public class ChildUpStairsCombinationCounter {
    public static void main(String[] args) {
        int steps = 3;
        var cache = new int[steps + 1];
        Arrays.fill(cache, -1);
        System.out.printf("Count ways for %s, %s %n", steps, countWaysFirstSolution(steps, cache));
    }

    // recursion
    private static int countWaysFirstSolution(int steps, int[] cache) {
        if (steps < 0) {
            return 0;
        } else if (steps == 0) {
            return 1;
        } else if (cache[steps] > -1) {
            return cache[steps];
        } else {
            cache[steps] = countWaysFirstSolution(steps - 1, cache)
                    + countWaysFirstSolution(steps - 2, cache)
                    + countWaysFirstSolution(steps - 3, cache);
            return cache[steps];
        }
    }
}
