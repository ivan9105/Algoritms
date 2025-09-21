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

        var pivot = from + (to - from) / 2;
        var middleValue = arr[pivot];
        var leftIndex = from;
        var rightIndex = to;

        while (leftIndex <= rightIndex) {
            while (arr[leftIndex] < middleValue) {
                leftIndex++; // означает что значение в правильном подмассиве
            }

            while (arr[rightIndex] > middleValue) {
                rightIndex--; // означает что значение в правильном подмассиве
            }

            if (leftIndex <= rightIndex) { // тут мы дошли до элементов которые находяться не в своих половинах
                swap(arr, leftIndex, rightIndex);
                leftIndex++;
                rightIndex--;
            }
        }

        if (from < rightIndex) {
            quickSort(arr, from, rightIndex); // делаем тоже самое для неосортированных элементов подмассива
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
