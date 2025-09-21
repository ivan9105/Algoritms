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
            var board = new char[n][n];
            for (var row = 0; row < n; ++row) {
                for (int col = 0; col < n; ++col)
                    board[row][col] = '.';
            }

            var leftRow = new int[n];

            // направление удара
            var lowerDiagonal = new int[2 * n - 1];
            var upperDiagonal = new int[2 * n - 1];

            var res = new ArrayList<List<String>>();
            calculate(0, board, res, leftRow, lowerDiagonal, upperDiagonal);

            return res;
        }

        public void calculate(int col, char[][] board, List<List<String>> res, int[] leftRow, int[] lowerDiagonal, int[] upperDiagonal) {
            if (col == board.length){
                var ans = new ArrayList<String>();
                for (char[] chars : board) {
                    var position = new String(chars);
                    ans.add(position);
                }
                res.add(ans);
                return;
            }

            // backtracking по каждой ячейки доски
            for(var row = 0; row < board.length; ++row) {
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
