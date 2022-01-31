package com.leetcode.concurrent;

public class RichestCustomerWealthProblem {
    public static void main(String[] args) {

    }

    /**
     * не знаю как это в блок concurrent упало, но сделаю
     */
    class Solution {
        public int maximumWealth(int[][] accounts) {
            int maxWealth = -1;
            for (int i = 0; i < accounts.length; i++) {
                int[] row = accounts[i];

                int sum = 0;
                for (int v : row) {
                    sum += v;
                }

                if (sum > maxWealth) {
                    maxWealth = sum;
                }
            }

            return maxWealth;
        }
    }
}
