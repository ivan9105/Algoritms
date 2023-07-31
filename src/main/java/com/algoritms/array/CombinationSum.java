package com.algoritms.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationSum {
    /**
     * Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.
     * <p>
     * The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the
     * frequency
     * of at least one of the chosen numbers is different.
     * <p>
     * The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.
     * <p>
     * 1 <= candidates.length <= 30
     * 2 <= candidates[i] <= 40
     * All elements of candidates are distinct.
     * 1 <= target <= 40
     * <p>
     * Input: candidates = [2,3,6,7], target = 7
     * Output: [[2,2,3],[7]]
     * Explanation:
     * 2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
     * 7 is a candidate, and 7 = 7.
     * These are the only two combinations.
     * <p>
     * Алгоритм использует "backtracking"
     */
    public static void main(String[] args) {
        var result = new ArrayList<List<Integer>>();
        var input = new int[]{2, 3, 6, 7};
        var target = 7;

        calculate(result, target, input, new ArrayList<>(), 0);

        System.out.printf("All set of combinations sum of arr: %s with target: %s %n", Arrays.toString(input), target);
        result.forEach(System.out::println);
    }

    private static void calculate(List<List<Integer>> result, int target, int[] input, List<Integer> output, Integer index) {
        if (target == 0) {
            result.add(new ArrayList<>(output));
            return;
        }

        //выходное условие
        if (input.length == index) {
            return;
        }

        var diff = target - input[index];
        if (diff >= 0) {
            //учитываем дубль
            output.add(input[index]);
            calculate(result, diff, input, output, index);
            output.remove(output.size() - 1);
        }
        calculate(result, target, input, output, index + 1);
    }
}
