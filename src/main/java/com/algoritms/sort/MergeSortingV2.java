package com.algoritms.sort;

import static java.lang.System.arraycopy;

import java.util.Arrays;

public class MergeSortingV2 {
    public static void main(String[] args) {
        int[] arr = {4, 9, 7, 6, 2, 3};
        sort(arr);
    }

    private static void sort(int[] arr) {
        System.out.println("Дано: " + prettyArr(arr));

        mergeSort(arr, 0, arr.length - 1);

        System.out.println("Получилось: " + prettyArr(arr));
    }

    //рекурсивно делим массив на 2 части по середине и делаем операцию мерж
    private static void mergeSort(int[] arr, int from, int to) {
        if (from >= to || to - from < 2) {
            return;
        }

        var median = (from + to) / 2;

        mergeSort(arr, from, median);
        mergeSort(arr, median, to);
        merge(arr, from, median, to);
    }

    /**
     * merge ( упорядоченные последовательности A, B , буфер C ) {
     * пока A и B непусты {
     * cравнить первые элементы A и B
     * переместить наименьший в буфер
     * }
     * если в одной из последовательностей еще есть элементы
     * дописать их в конец буфера, сохраняя имеющийся порядок
     * }
     */
    private static void merge(int[] arr, int from, int median, int to) {
        var leftIndex = from;
        var rightIndex = median;
        var tempIndex = 0;
        int tempLength = to - from + 1;
        var tempArr = new int[tempLength];

        //мерж
        while (leftIndex <= median && rightIndex <= to) {
            if (arr[leftIndex] < arr[rightIndex]) {
                tempArr[tempIndex++] = arr[leftIndex++];
            } else {
                tempArr[tempIndex++] = arr[rightIndex++];
            }
        }

        //дописываем
        while (rightIndex < to) {
            tempArr[tempIndex++] = arr[rightIndex++];
        }

        while (leftIndex < median) {
            tempArr[tempIndex++] = arr[leftIndex++];
        }

        //буфер в arr
        if (tempLength >= 0) arraycopy(tempArr, 0, arr, from, tempLength);
    }

    private static String prettyArr(int[] arr) {
        return Arrays.toString(arr);
    }
}
