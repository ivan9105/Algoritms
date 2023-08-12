package com.algoritms.sort;

import java.util.Arrays;

public class InsertSort {
    public static void main(String[] args) {
        int[] arr = {4, 9, 7, 6, 2, 3};
        sort(arr);
    }

    private static void sort(int[] arr) {
        int size = arr.length;

        System.out.println("Дано: " + prettyArr(arr));

        // скок элементов сток и проходов
        for (int i = 1; i < size; i++) {
            int j = i - 1;
            // внутренний проход
            // смысл такой элементы отсортированные слева (вначале массива)
            // берем элемент след по итерации слева с неотсортированного куска
            // сравниваем с теми что отсортированы и меняем их местами до тех пор пока не дойдем до начала массива
            // до тех пор пока текущий элемент меньше чем те что отсортированы
            // j + 1 - позиция элемента который сравниваем
            while (j >= 0 && arr[j] > arr[j + 1]) {
                swap(arr, j, j + 1);

                System.out.println("       После " + j + " внутренней итерации: " + prettyArr(arr));


                j--;
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
