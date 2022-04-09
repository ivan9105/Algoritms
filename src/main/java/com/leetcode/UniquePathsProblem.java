package com.leetcode;

import java.util.Arrays;

public class UniquePathsProblem {
    public static void main(String[] args) {
        System.out.println(new Solution().uniquePaths(3, 7));
    }

    static class Solution {
        private int[][] arr = new int[102][102];

        /**
         * Для каждого m и n находим кол-во уникальных путей, в качестве проверке на уникальность используем массив - общий
         */
        public int uniquePaths(int m, int n) {
            if(arr[m][n] != 0) return arr[m][n];
            if(m < 1 || n < 1) return 0;
            if(m == 1 && n == 1) return 1;
            arr[m][n] = uniquePaths(m - 1, n) + uniquePaths(m, n-1);
            return arr[m][n];
        }
    }
}
