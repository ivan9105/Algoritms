package com.algoritms.array;

//Given an integer array nums, find the contiguous subarray (containing at least one number)
//which has the largest sum and return its sum.
//
//A subarray is a contiguous part of an array.
//
//Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
//Output: 6
//Explanation: [4,-1,2,1] has the largest sum = 6
//Сложность должна быть n.

import java.util.Arrays;

public class MaximumSubArray {
    public static void main(String[] args) {
        calculateAndPrint(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4});
        calculateAndPrint(new int[]{5, 4, -1, 7, 8});
    }

    private static void calculateAndPrint(int[] arr) {
        int maxSum = calculate(arr);
        System.out.println("Max sum of arr: " + Arrays.toString(arr) + " is: " + maxSum);
    }

    private static int calculate(int[] arr) {
        var maxSum = Integer.MIN_VALUE;
        var currentSum = 0;

        for (int value : arr) {
            currentSum += value;

            if (currentSum > maxSum) {
                maxSum = currentSum;
            }

            if (currentSum < 0) {
                currentSum = 0;
            }
        }

        return maxSum;
    }
}
