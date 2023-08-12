package com.algoritminput.strings;

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
        int length = input.length();
        int maxLength = 0;
        Set<Character> charSet = new HashSet<>();
        int left = 0;

        for (int right = 0; right < length; right++) {
            if (!charSet.contains(input.charAt(right))) {
                charSet.add(input.charAt(right));
                maxLength = Math.max(maxLength, right - left + 1);
            } else {
                while (charSet.contains(input.charAt(right))) {
                    charSet.remove(input.charAt(left));
                    left++;
                }
                charSet.add(input.charAt(right));
            }
        }

        return maxLength;
    }

}
