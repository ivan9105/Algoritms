package com.algoritms.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * Напишите метод, возвращающий все подмножества заданного множества.
 */
public class AllSubsets {
    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4);

        System.out.println("All sub lists of " + list + ": " + generate(list, 0));
    }

    private static List<List<Integer>> generate(List<Integer> list, int index) {
        List<List<Integer>> result;

        if (list.size() == index) {
            result = new ArrayList<>();
            result.add(new ArrayList<>());
        } else {
            result = generate(list, index + 1);
            var value = list.get(index);

            List<List<Integer>> temp = new ArrayList<>();
            for (List<Integer> subList : result) {
                List<Integer> nextSublist = new ArrayList<>(subList);
                nextSublist.add(value);
                temp.add(nextSublist);
            }

            result.addAll(temp);
        }

        return result;
    }
}