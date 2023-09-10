package com.algoritms.dynamic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Реализуйте алгоритм для вывода всех корректных (правильно открытых и закрытых)
 * комбинаций из п пар круглых скобок.
 * Пример:
 * Ввод: 3
 * Вывод: (( () ) ) , ( () () ) , ( ()) () , () ( () ) , () () ()
 */
public class UniqueBracketsGenerator {
    public static void main(String[] args) {
        int count = 3;

        System.out.println("Generate result with count: " + count + ": " + calc(count));
    }

    private static List<String> calc(int count) {
        var buffer = new char[count * 2];
        var result = new ArrayList<String>();

        calc(result, count, count, buffer, 0);

        return result;
    }

    //backtracking
    private static void calc(List<String> result, int leftCount, int rightCount, char[] buffer, int count) {
        if (leftCount < 0 || rightCount < leftCount) {
            return;
        }

        if (leftCount == 0 && rightCount == 0) {
            var newStr = String.copyValueOf(buffer);
            result.add(newStr);
            return;
        }

        if (leftCount > 0) {
            buffer[count] = '(';
            calc(result, leftCount - 1, rightCount, buffer, count + 1);
        }

        if (rightCount > leftCount) {
            buffer[count] = ')';
            calc(result, leftCount, rightCount - 1, buffer, count + 1);
        }
    }

    // O (n!)
    private static Set<String> calcWithRec(int count) {
        var result = new HashSet<String>();
        if (count == 0) {
            result.add("");
        } else {
            var words = calcWithRec(count - 1);
            for (String word : words) {
                for (int index = 0; index < word.length(); index++) {
                    if (word.charAt(index) == '(') {
                        var newStr = insertBrackets(word, index);
                        result.add(newStr);
                    }
                }
                result.add("()" + word);
            }
        }

        return result;
    }

    private static String insertBrackets(String word, int position) {
        var start = word.substring(0, position + 1);
        var end = word.substring(position + 1);
        return start + "()" + end;
    }
}
