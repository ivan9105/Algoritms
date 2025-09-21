package com.algoritms.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * Напишите метод, возвращающий все подмножества заданного множества.
 */
public class AllSubsets {
    public static void main(String[] args) {
        var list = List.of(1, 2, 3, 4);

        System.out.println("All sub lists of " + list + ": " + backtrack(list));
    }

    // динамический подход
    private static List<List<Integer>> generate(List<Integer> list, int index) {
        List<List<Integer>> result;

        if (list.size() == index) {
            result = new ArrayList<>();
            result.add(new ArrayList<>()); // начальное значение, потом начинается расчет, по стеку вызовов
        } else {
            result = generate(list, index + 1);
            var value = list.get(index); // первый вызов будет с индексом 3, потому что по стеку это первое возвращаемое значение с пустым множеством выше

            // делаем расчет на основе предыдущих результатов и обогащаем значением value(по стеку будет 4, 3 ... etc)
            var temp = new ArrayList<List<Integer>>();
            for (List<Integer> subList : result) {
                var nextSublist = new ArrayList<>(subList);
                nextSublist.add(value);
                temp.add(nextSublist);
            }
            result.addAll(temp);
        }

        return result;
    }

    private static List<List<Integer>> backtrack(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(list, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(List<Integer> list, int start, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path));
        for (int index = start; index < list.size(); index++) {
            path.add(list.get(index));
            backtrack(list, index + 1, path, result);
            path.remove(path.size() - 1);
        }
    }
}