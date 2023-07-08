package com.algoritms.sort;

import java.util.Arrays;

public class SelectSort {
    public static void main(String[] args) {
        int[] arr = {4, 9, 7, 6, 2, 3};
        sort(arr);
    }

    private static void sort(int[] arr) {
        int size = arr.length;

        System.out.println("Дано: " + prettyArr(arr));

        // кол - во элементов
        for (int i = 0; i < size; i++) {
            int min = arr[i];
            int minIndex = i;
            // внутренний проход - отсортированные элементы скапливаются в самом начале
            // идем с самого начала ищем мин элемент меняем местами с самым первым
            // двигаем внутренний цикл
            for (int j = i + 1; j < size; j++) {
                if (min > arr[j]) {
                    min = arr[j];
                    minIndex = j;
                }
            }

            swap(arr, minIndex, i);
            System.out.println("После " + i + " итерации: " + prettyArr(arr));

        }
    }

    private static void swap(int[] arr, int firstIndex, int secondIndex) {
        var temp = arr[secondIndex];
        arr[secondIndex] = arr[firstIndex];
        arr[firstIndex] = temp;
    }

    private static String prettyArr(int[] arr) {
        return Arrays.toString(arr);
    }
}
