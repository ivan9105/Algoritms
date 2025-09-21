package com.algoritms.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Дано неограниченное количество монет достоинством 25, 10, 5 и 1 цент. Напишите
 * код, определяющий количество способов представления п центов.
 */
public class GiveChangeCalculator {
    public static void main(String[] args) {
        var denominations = new int[]{25, 10, 5, 1};
        var amount = 100;
        var result = new ArrayList<List<Integer>>();

        calculate(result, amount, denominations, new ArrayList<>(), 0);

        result.forEach(System.out::println);
    }

    private static void calculate(List<List<Integer>> result, int amount, int[] denominations, List<Integer> output, int index) {
        if (amount == 0) {
            result.add(new ArrayList<>(output));
            return;
        }

        if (denominations.length == index) {
            return;
        }

        var diff = amount - denominations[index];
        if (diff >= 0) {
            output.add(denominations[index]);
            calculate(result, diff, denominations, output, index);
            output.remove(output.size() - 1); // посчитали все возможные варианты удаляем элемент из списка тот что был добавлен
        }
        calculate(result, amount, denominations, output, index + 1);
    }


}
