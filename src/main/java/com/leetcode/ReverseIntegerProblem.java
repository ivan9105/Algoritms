package com.leetcode;

public class ReverseIntegerProblem {
    public static void main(String[] args) {
        System.out.println(new PopAndPushDigitsSolution().reverse(-123));
    }

    /**
     * Example 1:
     *
     * Input: x = 123
     * Output: 321
     * Example 2:
     *
     * Input: x = -123
     * Output: -321
     * Example 3:
     *
     * Input: x = 120
     * Output: 21
     *
     * // берем остаток числа от 10, запоминаем
     * // делим число на 10
     * // результат = результат * 10 + остаток
     * // итак до тех пор пока число != 0
     */
    static class PopAndPushDigitsSolution {
        public int reverse(int x) {
            var res = 0;
            while (x != 0) {
                var remain = x % 10;
                x /= 10;
                if (isPositiveIntLimitExceeded(res, remain)) {
                    return 0;
                }

                if (isNegativeLimitExceeded(res, remain)) {
                    return 0;
                }

                res = res * 10 + remain;
            }

            return res;
        }

        private static boolean isNegativeLimitExceeded(int res, int remain) {
            return res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && remain < -8);
        }

        private static boolean isPositiveIntLimitExceeded(int res, int remain) {
            return res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && remain > 7);
        }
    }

    /**
     * String builder простое решение
     */
    static class SimpleSolution {
        public int reverse(int x) {
            String resultStr;
            if (x < 0) {
                resultStr = new StringBuilder(String.valueOf(-x)).append("-").reverse().toString();
            } else {
                resultStr = new StringBuilder(String.valueOf(x)).reverse().toString();
            }

            try {
                return Integer.parseInt(resultStr);
            } catch (NumberFormatException nfe) {
                //if the string does not contain a parsable integer.
                return 0;
            }
        }
    }
}
