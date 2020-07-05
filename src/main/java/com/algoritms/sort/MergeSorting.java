package com.algoritms.sort;

import java.util.Arrays;
import java.util.Random;

import static java.util.Arrays.copyOfRange;

public class MergeSorting {
    /**
     * Делим массив на 2 части рекурсивно до тех пора пока делиться или threshold позволяет это сделать
     * после рекурсивно делаем merge
     */
    private int[] mergeSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int middle = array.length / 2;
        int[] leftArray = copyOfRange(array, 0, middle);
        int[] rightArray = copyOfRange(array, middle, array.length);
        return merge(mergeSort(leftArray), mergeSort(rightArray));
    }

    private int[] merge(int[] leftArray, int[] rightArray) {
        int leftLength = leftArray.length;
        int rightLength = rightArray.length;
        int leftIndex = 0, rightIndex = 0, newLength = leftLength + rightLength;
        int[] sortedArray = new int[newLength];

        for (int position = 0; position < newLength; position++) {
            if (leftIndex < leftLength && rightIndex < rightLength) {
                if (leftArray[leftIndex] > rightArray[rightIndex]) {
                    sortedArray[position] = rightArray[rightIndex++];
                } else {
                    sortedArray[position] = leftArray[leftIndex++];
                }
            } else if (rightIndex < rightLength) {
                sortedArray[position] = rightArray[rightIndex++];
            } else {
                sortedArray[position] = leftArray[leftIndex++];
            }
        }
        return sortedArray;
    }

    public static void main(String[] args) {
        MergeSorting mergeSorting = new MergeSorting();
        Random random = new Random();

        int[] unsortedArray = new int[10];
        for (int i = 0; i < 10; i++) {
            unsortedArray[i] = random.nextInt(1000);
        }
        System.out.println(String.format("Unsorted array: %s", Arrays.toString(unsortedArray)));
        System.out.println(String.format("Sorted array: %s", Arrays.toString(mergeSorting.mergeSort(unsortedArray))));
    }
}
