package com.algoritms.sort;

import java.util.Arrays;

public class BottleSort {
    public static void main(String[] args) {
        int[] arr = {4, 9, 7, 6, 2, 3};
        sort(arr);
    }

    private static void sort(int[] arr) {
        int size = arr.length;

        System.out.println("Дано: " + prettyArr(arr));

        //сколько элементов в массиве - столько раз и выполнили проходов
        for (int i = 0; i < size; i++) {
            //исключаем отсортированные элементы спереди и проход массива делаем с конца
            //i - кол-во отсортированных уже элементов с начала массива
            //j - индекс прохода массива с конца
            for (int j = size - 1; j > i; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                }
            }

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
