package com.algoritms.array;

import java.util.ArrayList;
import java.util.List;

/**
 * Input: array = {1, 2, 3}
 * Output: // this space denotes null element.
 * 1
 * 1 2
 * 1 2 3
 * 1 3
 * 2
 * 2 3
 * 3
 * Explanation: These are all the subsets that
 * can be formed using the array.
 * Input: 1 2
 * Output:
 * 1
 * 2
 * 1 2
 * Explanation: These are all the subsets that
 * can be formed using the array.
 */
public class BacktrackingAlgorithmExample {
    /**
     * Алгоритм:
     * <p>
     * 1) Создать рекурсивную фнукцию которая принимает:
     * входной массив,
     * выходной массив - текущее уникальное подмножество,
     * индекс в списке,
     * результат - список подмножеств
     * 2) В функции проходимся от 0 до n-1
     * 3) Перебираем по факту все возможные элементы рекурсивно с увеличением индекса и без
     */
    public static void main(String[] args) {
        var input = List.of(1, 2, 3);
        var result = new ArrayList<List<Integer>>();

        calculate(result, input, new ArrayList<>(), 0);
    }

    private static void calculate(List<List<Integer>> subsets, List<Integer> input, List<Integer> output, Integer index) {
        System.out.printf("Call calc, Index: %s, Input: %s, Output: %s %n", index, input, output);

        if (index == input.size()) {
            System.out.printf("Add subset, Index: %s, Input: %s, Output: %s %n", index, input, output);
            subsets.add(output);
            return;
        }

        calculate(subsets, input, new ArrayList<>(output), index + 1);

        output.add(input.get(index));
        calculate(subsets, input, new ArrayList<>(output), index + 1);
    }
}
