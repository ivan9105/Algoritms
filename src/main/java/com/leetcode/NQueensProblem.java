package com.leetcode;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class NQueensProblem {
    public static void main(String[] args) {
        System.out.println(new Solution().solveNQueens(4));
    }

    /**
     * Input: n = 4
     * Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
     */
    static class Solution {
        public List<List<String>> solveNQueens(int n) {
            char[][] board = new char[n][n];
            for (int row = 0; row < n; ++row) {
                for (int col = 0; col < n; ++col)
                    board[row][col] = '.';
            }

            int[] leftRow = new int[n];
            /**
             * Размер диагоналей обусловлен вариативностью значений, так как у нас доска имеет фиксированный размер
             * по факту это не является диагональю в классическом смысле
             *
             * нам нужно чтобы ни одна королева не могла достать до другой королевы
             *
             * первое ограничение - leftRow - это по факту номер строки, у них не должно быть одинаковых строк
             *
             * второе "диагонали" которые учитывают кейсы с номером столбца
             * к примеру наша фигура находиться на x = 1, y = 0 - нижней диагонали будет высчитываться по принципу x + y = 1
             * если мы хотим проверить фигуру по координатам x = 0, y = 1, по факту будет конфликт
             * вариативность значений возможно n * 2
             * кейса с 8 у нас никогда не бывает так как в данном случае сработает выходное условие номер колонки = 4
             *
             * по этой причине достаточно n * 2 - 1
             */


            int[] lowerDiagonal = new int[2 * n - 1];
            int[] upperDiagonal = new int[2 * n - 1];

            List<List<String>> res = new ArrayList<>();
            calculate(0, board, res, leftRow, lowerDiagonal, upperDiagonal);

            return res;
        }

        public void calculate(int col, char[][] board, List<List<String>> res, int[] leftRow, int[] lowerDiagonal, int[] upperDiagonal) {
            if(col == board.length){
                List<String> ans = new ArrayList<>();
                for(int row = 0; row < board.length; ++row){
                    String s = new String(board[row]);
                    ans.add(s);
                }
                res.add(ans);
                return;
            }

            /**
             * По факту мы перебираем все возможные варианты
             *
             * кол-во возможных вариантов n * n
             *
             * Проверяем что по линии (row - слева направо) нет совпадений
             * Проверяем то что по диагоналям нет пересечений обновляем состояние
             * Так как мы двигаемся рекурсивно
             * Мы гарантированно проходим все возможные варианты с 0 до 4 (не включая)
             * И собираем все возможные варианты которую могут быть
             */
            for(int row = 0; row < board.length; ++row) {
                if (leftRow[row] == 0 && lowerDiagonal[row + col] == 0 && upperDiagonal[board.length - 1 + col - row] == 0) {
                    board[row][col] = 'Q';
                    leftRow[row] = 1;
                    lowerDiagonal[row + col] = 1;
                    upperDiagonal[board.length - 1 + col - row] = 1;
                    calculate(col + 1, board, res, leftRow, lowerDiagonal, upperDiagonal);
                    board[row][col] = '.';
                    leftRow[row] = 0;
                    lowerDiagonal[row + col] = 0;
                    upperDiagonal[board.length - 1 + col - row] = 0;
                }
            }
        }
    }
}
