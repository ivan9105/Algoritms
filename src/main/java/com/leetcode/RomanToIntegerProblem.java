package com.leetcode;

import java.util.HashMap;
import java.util.Map;

public class RomanToIntegerProblem {
    public static void main(String[] args) {
        System.out.println(new SimpleSolution().romanToInt("LVIII"));
    }

    /**
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

        /**
         * I can be placed before V (5) and X (10) to make 4 and 9.
         * X can be placed before L (50) and C (100) to make 40 and 90.
         * C can be placed before D (500) and M (1000) to make 400 and 900.
         */
        private static final Map<String, Integer> RULES_VALUES_MAP = new HashMap<>() {{
            put("IV", 4);
            put("IX", 9);
            put("XL", 40);
            put("XC", 90);
            put("CD", 400);
            put("CM", 900);
        }};


        public int romanToInt(String str) {
            int sum = 0;
            String reversedStr = new StringBuilder(str).reverse().toString();
            char lastChar = '-';

            for (int i = 0; i < reversedStr.length(); i++) {
                char currentChar = reversedStr.charAt(i);
                Integer ruleValue = getRuleValue(currentChar, lastChar);

                if (ruleValue != null) {
                    sum = sum + (ruleValue - ROMAN_NUMBERS_WEIGHT_MAP.get(lastChar));
                } else {
                    sum += ROMAN_NUMBERS_WEIGHT_MAP.get(currentChar);
                }

                lastChar = currentChar;
            }

            return sum;
        }

        private Integer getRuleValue(char currentChar, char lastChar) {
            char[] rule = new char[2];
            rule[0] = currentChar;
            rule[1] = lastChar;
            return RULES_VALUES_MAP.get(new String(rule));
        }


    }
}
