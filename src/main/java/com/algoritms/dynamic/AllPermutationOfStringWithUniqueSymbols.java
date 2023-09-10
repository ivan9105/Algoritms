package com.algoritms.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * Напишите метод для вычисления всех перестановок строки, состоящей из
 * уникальных символов.
 */
public class AllPermutationOfStringWithUniqueSymbols {
    public static void main(String[] args) {
        var str = "abc";

        System.out.println("All permutations of '" + str + "': " + calcSecond(str));
    }

    // O(n!) - тоже
    private static List<String> calcSecond(String str) {
        var result = new ArrayList<String>();
        calcSecond("", str, result);
        return result;
    }

    private static void calcSecond(String prefix, String subStr, List<String> result) {
        if (subStr.length() == 0) {
            result.add(prefix);
        }

        int length = subStr.length();
        for (int index = 0; index < length; index++) {
            var before = subStr.substring(0, index);
            var after = subStr.substring(index + 1, length);
            char ch = subStr.charAt(index);
            calcSecond(prefix + ch, before + after, result);
        }
    }

    // O(n!)
    private static List<String> calcFirst(String str) {
        var result = new ArrayList<String>();
        if (str.length() == 0) {
            result.add("");
            return result;
        }

        var first = str.charAt(0);
        var subStr = str.substring(1);
        var words = calcFirst(subStr);
        for (String word : words) {
            for (int index = 0; index <= word.length(); index++) {
                var newStr = insertChar(word, first, index);
                result.add(newStr);
            }
        }

        return result;
    }

    private static String insertChar(String word, char ch, int position) {
        var start = word.substring(0, position);
        var end = word.substring(position);
        return start + ch + end;
    }
}
