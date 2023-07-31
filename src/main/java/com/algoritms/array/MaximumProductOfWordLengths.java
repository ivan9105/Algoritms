package com.algoritms.array;

//Given a string array words, return the maximum value of length(word[i]) * length(word[j])
//where the two words do not share common letters.
//If no such two words exist, return 0.
//
//Input: words = ["abcw","baz","foo","bar","xtfn","abcdef"]
//Output: 16
//Explanation: The two words can be "abcw", "xtfn".
//from typing import List

import java.util.ArrayList;
import java.util.List;

public class MaximumProductOfWordLengths {
    public static void main(String[] args) {
        // решение через побитовую маску "|" - побитовое ИЛИ, "<<" - СДВИГ влево, "&" - побитовый И
        // сдвиг влево на какое то кол-во
        // пример 1 << 2 == 1 -> 100 = 8
        // побитовое ИЛИ - 2 числа в двоичном представлении - ЕСЛИ хоть 1 разряд == 1 то результат значение рязряда 1 если оба 0 то == 0
        // побитовый И - 2 числа в двоичном представлении - ЕСЛИ оба разряда 1 == 1 ИНАЧЕ 0
        calculateAndPrint(new String[]{"abcw", "baz", "foo", "bar", "xtfn", "abcdef"});
        calculateAndPrint(new String[]{"a", "ab", "abc", "d", "cd", "bcd", "abcd"});
        calculateAndPrint(new String[]{"a", "aa", "aaa", "aaaa"});
    }

    private static void calculateAndPrint(String[] arr) {
        System.out.println("Maximum value of two not containing letters words: " + calculate(arr));
    }

    private static int calculate(String[] arr) {
        int length = arr.length;
        int[] masks = new int[length];
        var largest = 0;

        // init bit masks
        for (int index = 0; index < length; index++) {
            var word = arr[index];
            for (char c : word.toCharArray()) {
                masks[index] |= (1 << (c - 'a'));
            }
        }

        //n^2
        for (int index = 0; index < length - 1; index++) {
            for (int innerIndex = 0; innerIndex < length; innerIndex++) {
                if ((masks[index] & masks[innerIndex]) == 0) {
                    largest = Math.max(largest, arr[index].length() * arr[innerIndex].length());
                }
            }
        }

        return largest;
    }
}
