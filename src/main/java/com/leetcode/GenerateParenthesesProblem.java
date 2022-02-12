package com.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateParenthesesProblem {
    public static void main(String[] args) {
        System.out.println(new ClosureNumberSolution().generateParenthesis(2));
    }


    /**
     * Не мое решение
     * <p>
     * разбор:
     * с - current
     * n - общее кол-во скобок
     * делаем проход по кол - ву скобок
     * left - левый проход по рекурсивной функции передаем текущий current
     * right - правый проход по рекурсивной функции, начинаем с правой части генерацию формула общее кол-во - 1 - current
     * <p>
     * делаем конкатенацию всех возможных вариаций по ф-ле
     * ( + left + ) + right
     * <p>
     * на каждом этапе цикла у нас через рекурсию генерируются все возможные варианты, причем гарантии openCount == closeCount за счет функции конкатенации
     * <p>
     * n = 2
     * первый вызов (идем по левой части)
     * c = 0, left = "" (так как передаем первично 0), right = "" (n - 1 - 0) = 1 generateParenthesis(c) отдаст так же пустое значение
     * итого ( + left + ) + right = ()
     * <p>
     * второй вызов (идем по левой части)
     * с = 0, left = "", right = (), n = 2
     * итого ( + left + ) + right = () = ()()
     * <p>
     * третий вызов (идем по правой части)
     * c = 0, left = "", right = ""
     * итого
     * итого ( + left + ) + right = ()
     * <p>
     * четвертый вызов (идем по правой части)
     * c = 1, left = (), right = ""
     * итого ( + left + ) + right = (())
     * <p>
     */
    static class ClosureNumberSolution {
        public List<String> generateParenthesis(int n) {
            List<String> combinations = new ArrayList<>();
            if (n == 0) {
                combinations.add("");
            } else {
                for (int c = 0; c < n; ++c) {
                    List<String> lefts = generateParenthesis(c);
                    for (String left : lefts) {
                        List<String> rights = generateParenthesis(n - 1 - c);
                        for (String right : rights) {
                            String combination = "(" + left + ")" + right;
                            combinations.add(combination);
                        }
                    }
                }
            }
            return combinations;
        }
    }

    /**
     * Похожий алгоритм что и в SimpleSolution
     * <p>
     * Только с той разницей что у нас идет контроль закрывающих и открывающих скобок
     * <p>
     * так же особое внимание на deleteLast метод который удаляет последний элемент из string builder
     * так как он шариться на рекурсивный вызов работает это таким образом что удаляется лишний элемент гарантированно после заполнения size
     */
    static class BacktrackingSolution {
        private boolean isDebug = true;

        public List<String> generateParenthesis(int n) {
            List<String> combinations = new ArrayList<>();
            backtrack(combinations, new StringBuilder(), 0, 0, n);
            return combinations;
        }

        private void backtrack(List<String> combinations, StringBuilder sb, int openCount, int closeCount, int size) {
            if (isDebug) {
                System.out.println(sb.toString());
            }

            if (sb.length() == size * 2) {
                combinations.add(sb.toString());
                return;
            }

            if (openCount < size) {
                sb.append("(");
                backtrack(combinations, sb, openCount + 1, closeCount, size);
                deleteLast(sb);
            }

            if (closeCount < openCount) {
                sb.append(")");
                backtrack(combinations, sb, openCount, closeCount + 1, size);
                deleteLast(sb);
            }
        }

        private static void deleteLast(StringBuilder sb) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /**
     * Перебираем все возможные варианты через рекурсию
     * <p>
     * Рекурсивный алгоритм работает за счет порядка вызова функции и исп-и общего массива состояния
     */
    static class SimpleSolution {
        private boolean isDebug = false;

        public List<String> generateParenthesis(int n) {
            List<String> combinations = new ArrayList<>();
            generateAll(new char[2 * n], 0, combinations);
            return combinations;
        }

        public void generateAll(char[] arr, int pos, List<String> result) {
            boolean isValid = pos == arr.length && isValid(arr);

            if (isDebug) {
                System.out.printf("Result arr: %s is valid %s%n", Arrays.toString(arr), isValid);
            }

            if (pos == arr.length) {
                if (isValid) {
                    result.add(new String(arr));
                }
            } else {
                arr[pos] = '(';
                generateAll(arr, pos + 1, result);
                arr[pos] = ')';
                generateAll(arr, pos + 1, result);
            }
        }

        /**
         * Валидируем кол-во открывающихся и закрывающих скобок, кол-во открывшихся слева должно быть одинаковым
         * (()) - true
         * ())( - false
         */
        private static boolean isValid(char[] arr) {
            int balance = 0;

            for (char c : arr) {
                if (c == '(') {
                    balance++;
                } else {
                    balance--;
                }

                if (balance < 0) {
                    return false;
                }
            }

            return balance == 0;
        }
    }
}
