package com.leetcode;

public class LongestPalindromicSubstringProblem {
    public static void main(String[] args) {
        System.out.println(new ManachersAlgorithmSolution().longestPalindrome("cbbd"));
    }

    static class ManachersAlgorithmSolution {

        /**
         * статья https://e-maxx.ru/algo/palindromes_count
         * проходим в два проходы, для проверки двух типов полиндромоов
         * нечетный порядок - уменьшаем left на 1, увиличиваем right на 1, right изначально = текущий элемент и ищем максимальное кол-во совпадений
         * четный порядок - уменьшаем left на 1, увиличиваем right на 1, right изначально = текущий элемент + 1 и ищем максимальное кол-во совпадений
         * итак по каждому символу
         *
         */
        public String longestPalindrome(String str) {
            if (str == null || str.length() <= 1) {
                return str;
            }

            int start = 0, end = 0;
            int max = 1;
            int length = str.length();

            //нечетный порядок, полиндром вида - bab
            for (int i = 0; i < length - 1; i++) {
                int left = i, right = i;
                while (left >= 0 && right < length) {
                    if (str.charAt(left) == str.charAt(right)) {
                        left--;
                        right++;
                    } else {
                        break;
                    }
                }

                int result = right - left - 1;
                if (max < result) {
                    max = result;
                    start = left + 1;
                    end = right - 1;
                }
            }

            //четный порядок - палиндром вида dbbd
            for (int i = 0; i < length - 1; i++) {
                int left = i, right = i + 1;
                while (left >= 0 && right < length) {
                    if (str.charAt(left) == str.charAt(right)) {
                        left--;
                        right++;
                    } else {
                        break;
                    }

                }

                int result = right - left - 1;

                if (max < result) {
                    max = result;
                    start = left + 1;
                    end = right - 1;
                }
            }

            return str.substring(start, end + 1);
        }

        /**
         * Палиндром слово читающееся правильно в обоих направлениях пример "топот"
         * Палиндромы внутри "babad": "bab", "aba"
         * <p>
         * Как проверить что слово является полиндромом на ум приходит простое решение
         * str1 == reverse(str1) == true
         * <p>
         * Алгоримт со сложностью n^2 "Развернуть вокруг центра"
         * <p>
         * Проходимся по всему слову "топот"
         * <p>
         * Итерация 1
         * Начинаем с 0 позиции, пытаемся найти факт наличия полиндрома на вокруг текущего индекса i, либо следующего символа i + 1
         * вокруг текущего индекса мы ничего не находим так как слева нет символов, вокруг следующего 'о' аналогично
         * соотвественно палиндрома нет start, end == 0
         * <p>
         * Итерация 2
         * Начинаем уже с 1 позиции, пытаемся найти факт наличия полиндрома на вокруг текущего индекса i, либо следующего символа i + 1
         * вокруг текущего индекса мы не находим подходящих символов, справа тоже, но дефакто мы считаем палиндром в данном случае 'о'
         * вокруг след индеса мы ничего не находим
         * соотвестенно палиндром есть, его начальная позиция и конечная (интервал не включается) start = 1, end = 1
         * <p>
         * Итерация 3
         * Начинаем уже с 2 позиции
         * от текущего индекса символ 'п', мы начинаем двигаться влево и в право итеративно в итоге у нас совпадает символ с позицией 1 и 3, 0 и 4
         * соотвесвенно функция расширения от центра возвращает длину палиндрома 5
         * высчитываем индексы
         * start = текущая позиция (2) - длина (5 - 1) / 2 = 2 - 4 / 2 = 2 - 2 = 0
         * end = текущая позиция (2) + длина (5) / 2 = 4
         * <p>
         * Итерация 4
         * соотвественно идем дальше 3 позиция
         * кратко тут полиндрома нет поэтому start = 0, end = 4
         * <p>
         * Итерация 5
         * соотвественно идем дальше 4 позиция
         * кратко тут полиндрома нет поэтому start = 0, end = 4
         * <p>
         * функция делает substring и возвращает 'топот'
         */
        static class ExpandAroundCenterSolution {
            private final boolean isDebug = true;

            public String longestPalindrome(String str) {
                if (isDebug) {
                    System.out.printf("Start search longest palindrome %s %n", str);
                }

                if (str == null || str.length() < 1) return "";
                int start = 0, end = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (isDebug) {
                        System.out.printf("Iteration i = %d, substr = %s, start = %d, end = %d %n",
                                i, str.substring(0, i + 1), start, end);
                    }
                    int len1 = expandAroundCenter(str, i, i);
                    int len2 = expandAroundCenter(str, i, i + 1);
                    int len = Math.max(len1, len2);
                    if (isDebug) {
                        System.out.printf("Expand around center len1(%d, %d) = %d, len2(%d, %d) = %d, max = %d %n",
                                i, i, len1, i, i + 1, len2, len);
                    }
                    if (len > end - start) {
                        start = i - (len - 1) / 2;
                        end = i + len / 2;

                        if (isDebug) {
                            System.out.printf("start = %d, end = %d, substr = %s %n", start, end, str.substring(start, end + 1));
                        }
                    }
                    System.out.println();
                }
                return str.substring(start, end + 1);
            }

            private int expandAroundCenter(String str, int left, int right) {
                if (isDebug) {
                    System.out.printf("Expand around center str = %s, left = %d, right = %d, %n", str, left, right);
                }
                int L = left, R = right;
                long length = str.length();
                while (L >= 0 && R < length && str.charAt(L) == str.charAt(R)) {
                    char ch = str.charAt(L);

                    L--;
                    R++;

                    if (isDebug) {
                        System.out.printf("Found left/right same char, str = %s, left = %d, right = %d, ch = %s %n",
                                str, left, right, ch);
                    }
                }

                if (isDebug) {
                    System.out.printf("Expand around center result str = %s, left = %d, right = %d, res = %d %n",
                            str, left, right, R - L - 1);
                }

                return R - L - 1;
            }
        }
    }
}
