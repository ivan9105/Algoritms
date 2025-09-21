package com.leetcode;

import java.util.HashMap;
import java.util.Map;

public class RomanToIntegerProblem {
    public static void main(String[] args) {
        System.out.println(new SimpleSolution().romanToInt("MCMXCIV"));
    }

    /**
     *Example 1:
     *
     * Input: s = "III"
     * Output: 3
     * Explanation: III = 3.
     * Example 2:
     *
     * Input: s = "LVIII"
     * Output: 58
     * Explanation: L = 50, V= 5, III = 3.
     * Example 3:
     *
     * Input: s = "MCMXCIV"
     * Output: 1994
     * Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
     *
     */
    static class SimpleSolution {

        private static final Map<Character, Integer> ROMAN_NUMBERS_WEIGHT_MAP = new HashMap<>() {{
            put('I', 1);
            put('V', 5);
            put('X', 10);
            put('L', 50);
            put('C', 100);
            put('D', 500);
            put('M', 1000);
        }};

        private static final Map<String, Integer> RULES_VALUES_MAP = new HashMap<>() {{
            put("IV", 4);
            put("IX", 9);
            put("XL", 40);
            put("XC", 90);
            put("CD", 400);
            put("CM", 900);
        }};


        public int romanToInt(String str) {
            var sum = 0;
            var reversedStr = new StringBuilder(str).reverse().toString();
            var prevChar = '-';

            for (var index = 0; index < reversedStr.length(); index++) {
                var currentChar = reversedStr.charAt(index);
                var ruleValue = getRuleValue(currentChar, prevChar);

                if (ruleValue != null) {
                    //ruleValue - ROMAN_NUMBERS_WEIGHT_MAP.get(prevChar) - предыдущее значение не правильно посчитано, требуется обработать предыдущее суммирование
                    sum = sum + (ruleValue - ROMAN_NUMBERS_WEIGHT_MAP.get(prevChar));
                } else {
                    sum += ROMAN_NUMBERS_WEIGHT_MAP.get(currentChar);
                }

                prevChar = currentChar;
            }

            return sum;
        }

        private Integer getRuleValue(char currentChar, char lastChar) {
            var rule = new char[2];
            rule[0] = currentChar;
            rule[1] = lastChar;
            return RULES_VALUES_MAP.get(new String(rule));
        }


    }
}
