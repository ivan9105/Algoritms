package com.leetcode;

public class ReverseIntegerProblem {
    public static void main(String[] args) {
        System.out.println(new PopAndPushDigitsSolution().reverse(-321));
    }

    /**
     * Алгоритм:
     * для того чтобы перевернуть число за сложность O(log(x) необходимо делать перенос поразрядно
     * до тех пор пока число делиться на 10
     * мы вытаскиваем остаток от деления на 10 (последняя цифра)
     * далее делим текущий remain на 10 без остатка
     * в результат пишем = результат * 10 + остаток и так до тех пор пока число делиться
     * <p>
     * Исключительные ситуации
     * 1) если текущий результат положительный и результат > MAX INTEGER / 10
     * 2) для отрицательного результата наоборот
     * 3) если текущий результат положительный и результат == MAX INTEGER / 10 и remain > 7 (MAX INTEGER = 2147483647 соотвественно если остаток больше 7 это перелимит)
     * 4) если текущий результат отрицательный и результат == MIN INTEGER / 10 и remain < -8 (MIN INTEGER = -2147483648 соотвественно если остаток меньше -8 это перелимит)
     * возвращаем 0
     *
     * <p>
     * <p>
     * Пример -321
     * первый проход
     * pop = -321 % 10 = -1
     * x = -32
     * <p>
     * res = -1
     * <p>
     * второй проход
     * pop = -32 % 10 = -2
     * x = -3
     * <p>
     * res = -12
     * <p>
     * третий проход
     * pop = - 3 % 10 = -3
     * x = 0
     * <p>
     * res = -12 * 10 + (-3) = - 123
     */
    static class PopAndPushDigitsSolution {
        public int reverse(int x) {
            int res = 0;
            while (x != 0) {
                int remain = x % 10;
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
