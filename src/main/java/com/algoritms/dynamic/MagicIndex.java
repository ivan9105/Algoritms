package com.algoritms.dynamic;

import java.util.Arrays;

/**
 * Определим «волшебный » индекс для массива A[0."n-1] как индекс, для которого
 * выполняется условие A[i]=i. Для заданного отсортированного массива,
 * не содержащего одинаковых значений, напишите метод поиска «волшебного»
 * индекса в массиве А (если он существует).
 * Д ополиительио
 * Что произойдет, если массив может содержать одинаковые значения?
 */
public class MagicIndex {
    public static void main(String[] args) {
        //given
        var arr = new int[]{-10, -5, 2, 2, 2, 3, 4, 7, 9, 12, 13};

        var magicIndex = calc(arr, 0, arr.length - 1);

        System.out.println("The magic index of arr " + Arrays.toString(arr) + ": " + magicIndex);
    }

    private static int calc(int[] arr, int start, int end) {
        if (start > end) {
            return -1;
        }

        var middleIndex = (start + end) / 2;
        var middleValue = arr[middleIndex];
        if (middleIndex == middleValue) {
            return middleIndex;
        }

        var leftIndex = calc(arr, start, Math.min(middleIndex - 1, middleValue));
        if (leftIndex > 0) {
            return leftIndex;
        }

        return calc(arr, Math.max(middleIndex + 1, middleValue), end);
    }
}
