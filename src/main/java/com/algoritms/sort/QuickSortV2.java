package com.algoritms.sort;

import java.util.Arrays;

public class QuickSortV2 {
    public static void main(String[] args) {
        int[] arr = {4, 9, 7, 6, 2, 3};
        sort(arr);
    }

    private static void sort(int[] arr) {
        System.out.println("Дано: " + prettyArr(arr));

        quickSort(arr, 0, arr.length - 1);

        System.out.println("Получилось: " + prettyArr(arr));
    }

    private static void quickSort(int[] arr, int from, int to) {
        if (from >= to) {
            return;
        }

        var median = from + (to - from) / 2;
        var middleValue = arr[median];

        int leftIndex = from, rightIndex = to;
        while (leftIndex <= rightIndex) {
            while (arr[leftIndex] < middleValue) {
                leftIndex++;
            }

            while (arr[rightIndex] > middleValue) {
                rightIndex--;
            }

            if (leftIndex <= rightIndex) {
                System.out.printf("Swap left index: %s, right index: %s, median: %s, middle value: %s arr: %s%n",
                        leftIndex, rightIndex, median, middleValue, prettyArr(arr));

                swap(arr, leftIndex, rightIndex);

                System.out.printf("RESULT Swap left index: %s, right index: %s, median: %s, middle value: %s arr: %s%n",
                        leftIndex, rightIndex, median, middleValue, prettyArr(arr));

                leftIndex++;
                rightIndex--;
            }
        }

        if (from < rightIndex) {
            quickSort(arr, from, rightIndex);
        }

        if (to > leftIndex) {
            quickSort(arr, leftIndex, to);
        }

    }

    private static String prettyArr(int[] arr) {
        return Arrays.toString(arr);
    }

    private static void swap(int[] arr, int firstIndex, int secondIndex) {
        var temp = arr[secondIndex];
        arr[secondIndex] = arr[firstIndex];
        arr[firstIndex] = temp;
    }
}
