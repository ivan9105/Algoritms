package com.leetcode;


import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LongestSubstringWithoutRepeatingCharactersProblem {
    public static void main(String[] args) {
        System.out.println("Result: " + new DirectAccessTableSolution().lengthOfLongestSubstring("kwkerkk"));
    }


    /**
     *
     */
    static class DirectAccessTableSolution {

        public int lengthOfLongestSubstring(String str) {
            return lengthOfLongestSubstring(str, true);
        }

        /**
         * Оптимизация алгоритма скользящего окна, уменьшение кол-ва повторений x2
         * За счет одного цикла и избавление от hashmap-ы
         * Вместо этого у нас массив объектов Integer ограниченный лимитом ASCII 128 - макс размер индекса
         * алгоритм в точности как у SlidingWindowHashMapSolution,
         * ед разница что теперь мы в качестве ключа исп порядкой номер (int) current char и индекс первого элемента начинается с 0
         * Так же образуем окна за за счет right - позиция внутреннего цикла, left - index повторения
         */
        public int lengthOfLongestSubstring(String str, boolean isDebug) {
            if (isDebug) {
                System.out.println("Start search the length of the longest substring " + str);
            }

            Integer[] chars = new Integer[128];

            int left = 0;
            int right = 0;

            int res = 0;
            int length = str.length();
            while (right < length) {
                char r = str.charAt(right);

                if (isDebug) {
                    System.out.printf("Iteration right = %d, left = %d, current char = %s, substr = %s, arr = %s, res = %d %n",
                            right, left, r, str.substring(right, length), toString(chars), res
                    );
                }

                Integer index = chars[r];
                if (index != null && index >= left && index < right) {
                    left = index + 1;

                    if (isDebug) {
                        System.out.printf("Found repetition right = %d, left = %d, current char = %s, substr = %s, arr = %s, res = %d %n",
                                right, left, r, str.substring(right, length), toString(chars), res
                        );
                    }
                }

                res = Math.max(res, right - left + 1);

                if (isDebug) {
                    System.out.printf("Calculate max right = %d, left = %d, current char = %s, substr = %s, arr = %s, res = %d %n",
                            right, left, r, str.substring(right, length), toString(chars), res
                    );
                }

                chars[r] = right;
                right++;
            }

            return res;
        }

        private static String toString(Integer[] arr) {
            Map<Integer, Integer> resMap = new HashMap<>();
            AtomicInteger counter = new AtomicInteger();
            Arrays.stream(arr).forEach(it -> resMap.put(counter.incrementAndGet() - 1, it));
            String result = resMap.entrySet().stream()
                    .filter(it -> it.getValue() != null)
                    .map(it -> format("(int) current ch: %d, Index: %d", it.getKey(), it.getValue()))
                    .collect(joining("; "));
            return !result.isBlank() ? result : "-";
        }
    }


    /**
     * Оптимизация алгоритма скользящего окна, уменьшение кол-ва повторений x2
     * За счет одного цикла
     * Роль left выполняет индекс первичного вхождения по дублю за счет этого окно образуется без внутреннего цикла
     * <p>
     * kwkerkk
     * Итерация 1 k
     * RIGHT = 1, LEFT = 0, RES = 1, повторений нет [Index: k, value: 1], Окно 'k'
     * Итерация 2 w
     * RIGHT = 2, LEFT = 0, RES = 2, повторений нет [Index: w, value: 2; Index: k, value: 1], Окно 'kw'
     * Итерация 3 k
     * RIGHT = 3, повторения есть Index k, позиция 1
     * Двигаем окно LEFT = 1
     * RIGHT = 3, LEFT = 1, RES = 2, , Окно 'wk'
     * Итерация 4 e
     * RIGHT = 4, LEFT = 1, RES = 3, повторений нет [Index: e, value: 4; Index: w, value: 2; Index: k, value: 3] Окно 'wke'
     * Итерация 5 r
     * etc.
     */
    static class SlidingWindowHashMapSolution {
        public int lengthOfLongestSubstring(String str) {
            return lengthOfLongestSubstring(str, true);
        }

        public int lengthOfLongestSubstring(String str, boolean isDebug) {
            if (isDebug) {
                System.out.println("Start search the length of the longest substring " + str);
            }

            int n = str.length();
            int res = 0;

            Map<Character, Integer> map = new HashMap<>();

            for (int j = 0, i = 0; j < n; j++) {
                if (isDebug) {
                    System.out.printf("Iteration j = %d, i = %d, substr = %s, map = %s, res = %d %n",
                            j, i, str.substring(i, n), toString(map), res
                    );
                }

                char currentChar = str.charAt(j);
                //repetitions
                if (map.containsKey(currentChar)) {
                    if (isDebug) {
                        System.out.printf("Found repetition j = %d, i = %d, current char %s , substr = %s, map = %s, %n",
                                j, i, currentChar, str.substring(i, n), toString(map)
                        );
                    }
                    Integer repetitions = map.get(currentChar);
                    i = max(repetitions, i);
                }

                int diff = j - i + 1;
                res = max(res, diff);
                if (isDebug) {
                    System.out.printf("Calculate max j = %d, i = %d, current char %s , substr = %s, map = %s, res = %d, %n",
                            j, i, currentChar, str.substring(i, n), toString(map), res
                    );
                }
                map.put(currentChar, j + 1);
            }
            return res;
        }

        private static String toString(Map<Character, Integer> map) {
            return format("[%s]",
                    map.entrySet().stream()
                            .map(it -> format("Index: %s, value: %d", it.getKey(), it.getValue()))
                            .collect(joining(";")));
        }
    }

    static class SlidingWindowSolution {

        /**
         * Алгоритм скользящего окна
         * Сложность n^2
         * Проходимся внешним циклом по всему слову, собираем массив сиволов как внутренний кеш для того чтобы валидировать повторения
         * Храним позицию внешнего цикла в переменной right
         * Как только доходим до первого повторения, из массива символов удаляем данные до тех пор пока не дойдет до сивола дубля начиная с 0 позиции
         * Храним позицию внутреннего цикла в переменной left
         * Каждый проход по внешнему циклу записываем MAX(RES, right - left + 1)
         * Как это работает на примере
         * <p>
         * kwkerkk
         * <p>
         * первый проход внешнего цикла
         * RES = 0, RIGHT = 1, LEFT = 0, Повторений нет [Index: 107, value: 1]
         * RES = 1
         * <p>
         * второй проход внешнего цикла
         * RES = 1, RIGHT = 2, LEFT = 0, Повторений нет [Index: 107, value: 1; Index: 119, value: 1]
         * RES = 2
         * <p>
         * третий проход внешнего цикла, Повторения есть [Index: 107, value: 2; Index: 119, value: 1]
         * RES = 2, RIGHT = 3, LEFT = 0
         * Проходим по внутреннему циклу до удаления первого дубля 'k'
         * LEFT = 1
         * RES = 2
         * В итоге у нас получается окно начиная с первого символа новая подстрока
         * <p>
         * четвертый проход внешнего цикла, Повторений нет [Index: 101, value: 1; Index: 107, value: 1; Index: 119, value: 1]
         * RES = 2, RIGHT = 3, LEFT = 1
         * RES = RIGHT - LEFT + 1 = 3
         * <p>
         * пятый проход и т.д.
         */
        public int lengthOfLongestSubstring(String str) {
            return lengthOfLongestSubstring(str, true);
        }

        public int lengthOfLongestSubstring(String str, boolean isDebug) {
            if (isDebug) {
                System.out.println("Start search the length of the longest substring " + str);
            }
            //int[128] for ASCII -- American Standard Code for Information Interchange
            //попадаем в диапазоны с латинскими буквами и символами, поэтому размерность массива нам подходит
            int[] chars = new int[128];

            int left = 0;
            int right = 0;

            int res = 0;
            int length = str.length();
            while (right < length) {
                char r = str.charAt(right);
                //в массив с индексом (int) r икрементируем значение, пример
                //символ k в ASCII в int == 107, значение массива при первом проходе chars[107] == 1, при инициализации chars[107] = 0
                chars[r]++;

                if (isDebug) {
                    System.out.printf(
                            "Right cycle, right symbol %s, char to int %d, left %d, right %d, substr %s, chars arr [%s]%n",
                            r, (int) r, left, right, str.substring(right, length), toString(chars)
                    );
                }

                if (isDebug && chars[r] > 1) {
                    System.out.println("Start inner left cycle, cause we have repetition");
                }

                while (chars[r] > 1) {

                    char l = str.charAt(left);
                    System.out.printf("Left cycle, decrement pos %d, substr %s, chars arr [%s]%n",
                            (int) l, str.substring(right, length), toString(chars)
                    );
                    chars[l]--;
                    left++;
                    if (isDebug) {
                        System.out.printf(
                                "Left cycle, left symbol %s, char to int %d, left %d, right %d, substr %s, chars arr [%s]%n",
                                l, (int) l, left, right, str.substring(right, length), toString(chars)
                        );
                    }
                }

                int diff = right - left + 1;
                res = max(res, diff);
                if (isDebug) {
                    System.out.printf(
                            "Max = %d, left = %d, right = %d, right - left + 1 = %s, substring %s, chars arr [%s]%n",
                            res, left, right, diff, str.substring(right, length), toString(chars)
                    );
                }

                right++;
            }
            return res;
        }

        private static String toString(int[] arr) {
            Map<Integer, Integer> resMap = new HashMap<>();
            AtomicInteger counter = new AtomicInteger();
            Arrays.stream(arr).forEach(it -> resMap.put(counter.incrementAndGet() - 1, it));
            return resMap.entrySet().stream()
                    .filter(it -> it.getValue() != 0)
                    .map(it -> format("Index: %d, value: %d", it.getKey(), it.getValue()))
                    .collect(joining("; "));
        }
    }

    static class SimpleSolution {
        /**
         * Алгоритм:
         * проходимся полностью по всей строки в цикле причем в 3-ом соотвественно сложность 0 (n) в кубе
         * каждый проход ищем подстроку самой максимальной длины
         * <p>
         * начиная с 0 символа
         * 1-ого
         * 2-ого и так далее
         * <p>
         * Пример:
         * <p>
         * Start search the length of the longest substring kwkerkk
         * Found repetition in str kwkerkk substring kw, repetition k, repetition index 2, the longest size 2
         * Found repetition in str kwkerkk substring wker, repetition k, repetition index 4, the longest size 4
         * Found repetition in str kwkerkk substring ker, repetition k, repetition index 3, the longest size 4
         * Found repetition in str kwkerkk substring erk, repetition k, repetition index 3, the longest size 4
         * Found repetition in str kwkerkk substring rk, repetition k, repetition index 2, the longest size 4
         * Found repetition in str kwkerkk substring k, repetition k, repetition index 1, the longest size 4
         * Result: 4
         */
        public int lengthOfLongestSubstring(String str) {
            return lengthOfLongestSubstring(str, true);
        }

        public int lengthOfLongestSubstring(String str, boolean isDebug) {
            if (isDebug) {
                System.out.println("Start search the length of the longest substring " + str);
            }

            int n = str.length();

            int longestSubstringSize = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    if (!hasRepetition(str, i, j)) {
                        int currentLongestSubstringSize = j - i + 1;
                        longestSubstringSize = max(longestSubstringSize, currentLongestSubstringSize);
                    } else {
                        if (isDebug) {
                            String substring = str.substring(i, j);
                            char repeatedChar = str.charAt(j);
                            System.out.printf(
                                    "Found repetition in str %s substring %s, repetition %s, repetition index %d, the longest size %d%n",
                                    str,
                                    substring,
                                    repeatedChar,
                                    j - i,
                                    longestSubstringSize
                            );
                            break;
                        }
                    }
                }
            }

            return longestSubstringSize;
        }

        private boolean hasRepetition(String str, int start, int end) {
            char[] chars = new char[end - start + 1];
            if (chars.length == 0) {
                return false;
            }

            int counter = 0;
            for (int i = start; i <= end; i++) {
                char currentChar = str.charAt(i);

                if (hasDuplicateSymbol(chars, currentChar)) {
                    return true;
                }

                chars[counter++] = currentChar;
            }

            return false;
        }

        private boolean hasDuplicateSymbol(char[] chars, char checkedChar) {
            for (char existsChar : chars) {
                if (existsChar == checkedChar) {
                    return true;
                }
            }

            return false;
        }
    }
}
