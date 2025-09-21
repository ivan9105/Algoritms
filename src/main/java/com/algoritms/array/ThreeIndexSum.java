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

        sort(arr); // важно чтобы массив был отсортирован

        var length = arr.length;
        for (int current = 0; current < length - 2; current++) { // length - 2 последних элемента мы учтем во внутреннем цикле
            var left = current + 1; // две tmp переменные для расчета справа и слева
            var right = length - 1;

            while (left < right) {
                var sum = arr[left] + arr[right]; // сумма двух крайних элементов
                if (sum < -arr[current]) { // двигаем вправо
                    left++;
                } else if (sum > -arr[current]) { //двигаем влево
                    right--;
                } else {
                    result.add(List.of(arr[left], arr[current], arr[right]));
                    left++;
                    right--;
                }
            }
        }

        return result;
    }
}
