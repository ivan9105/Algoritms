package com.algoritms.array;

import static java.util.Arrays.sort;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ThreeIndexSum {
    //Given an integer array nums,
    //return all the triplets [nums[i], nums[j], nums[k]]
    //such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
    public static void main(String[] args) {
        calculateAndPrint(new int[]{-1, 0, 1, 2, -1, -4});
        calculateAndPrint(new int[]{0, 1, 1});
        calculateAndPrint(new int[]{0, 0, 0});
    }

    private static void calculateAndPrint(int[] arr) {
        System.out.println("Result of " + Arrays.toString(arr) + ": " + calculate(arr));
    }

    private static Set<List<Integer>> calculate(int[] arr) {
        if (arr == null || arr.length < 3) {
            return Set.of();
        }

        var result = new HashSet<List<Integer>>();

        sort(arr);

        var length = arr.length;

        for (int i = 0; i < length - 2; i++) {
            var j = i + 1;
            var k = length - 1;

            while (j < k) {
                var sum = arr[j] + arr[k];
                //если сумма меньше отрицательного элемента по индексу i - значит нужно подвинуть элемент слева j
                if (sum < -arr[i]) {
                    j++;
                    //если сумма больше отрицательного элемента по индексу i - значит нужно подвинуть элемент справа k
                } else if (sum > -arr[i]) {
                    k--;
                } else {
                    result.add(List.of(arr[i], arr[j], arr[k]));
                    j++;
                    k--;
                }
            }
        }

        return result;
    }
}
