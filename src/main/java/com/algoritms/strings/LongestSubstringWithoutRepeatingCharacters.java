package com.algoritminput.strings;

import static java.lang.Math.max;

import java.util.HashSet;
import java.util.Set;

//Given a string s, find the length of the longest substring without repeating characterinput.
//
//Input: s = "abcabcbb"
//Output: 3
//Explanation: The answer is "abc", with the length of 3.
//
//#Input: s = "pwwkemw"
//#Output: 4
public class LongestSubstringWithoutRepeatingCharacters {
    public static void main(String[] args) {
        calculateAndPrint("pwwkemw");
    }

    private static void calculateAndPrint(String input) {
        System.out.println("Max unique character substring in input: " + input + ", is: " + calculate(input));
    }

    /**
     * Подход с 2 индексами - левый и правый
     * Если символ уникален высчитываем max (left, right)
     * Если не уникален удаляем первый элемент в set так (двигаем подстроку)
     */
    private static int calculate(String input) {
        var inputArr = input.toCharArray();
        var left = 0;
        var maxLength = 0;
        var cache = new HashSet<Character>();

        for (var right = 0; right < inputArr.length; right++) {
            if (!cache.contains(inputArr[right])) {
                cache.add(inputArr[right]);
                maxLength = max(maxLength, right - left + 1);
            } else {
                while (cache.contains(inputArr[right])) {
                    cache.remove(input.charAt(left));
                    left++;
                }
                cache.add(input.charAt(right));
            }
        }

        return maxLength;
    }

}
