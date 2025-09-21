package com.leetcode;

public class NextPermutationProblem {
    public static void main(String[] args) {
        new NextPermutationProblem().nextPermutation(new int[]{1, 2, 3, 5, 4});
    }

    public void nextPermutation(int[] nums) {
        // на вход - {1, 2, 3, 5, 4}
        int pivot = -1;
        int swapIndex = -1;
        // точка поворота - обходим число с конца и ещим место где начальный элемент меньше следующего
        for (int index = nums.length - 2; index >= 0; index--) {
            if (nums[index] < nums[index + 1]) {
                pivot = index;
                break;
            }
        }
        // переворачиваем число
        if (pivot == -1) {
            reverse(nums, 0);
        } else {
            // второй индекс - индекс числа, которое больше индекса поворота - pivot = 2
            for (int index = nums.length - 1; index >= 0; index--) {
                if (nums[index] > nums[pivot]) {
                    swapIndex = index;
                    break;
                }
            }
            // меняем местами - индекс числа больше чем число поворота и число поворота - swapIndex = 4
            swap(nums, pivot, swapIndex);
            // {1, 2, 4, 5, 3}
            // меняем местами все числа начиная с индекса числа поворота + 1 - по факту правой половины
            reverse(nums, pivot + 1);
            // {1, 2, 4, 3, 5}
        }
    }

    void swap(int[] nums, int firstIndex, int secondIndex) {
        var temp = nums[firstIndex];
        nums[firstIndex] = nums[secondIndex];
        nums[secondIndex] = temp;
    }

    void reverse(int[] nums, int start) {
        var startIndex = start;
        var endIndex = nums.length - 1;
        while (startIndex < endIndex) {
            swap(nums, startIndex, endIndex);
            startIndex++;
            endIndex--;
        }
    }


}
