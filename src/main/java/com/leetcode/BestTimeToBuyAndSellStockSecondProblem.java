package com.leetcode;

public class BestTimeToBuyAndSellStockSecondProblem {
    public static void main(String[] args) {
        System.out.println(new SimpleSolution().maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
    }

    /**
     * Пример
     * Input: prices = [7,1,5,3,6,4]
     * Output: 7
     * Explanation: Buy on day 2 (price = 1) and sell on day 3 (price = 5), profit = 5-1 = 4.
     * Then buy on day 4 (price = 3) and sell on day 5 (price = 6), profit = 6-3 = 3.
     * Total profit is 4 + 3 = 7
     * <p>
     * Нужно посчитать максимальный profit при условии что ты должен купить и продать сразу на след день
     * Алгоритм по шагам:
     * <p>
     * TODO
     */
    static class SimpleSolution {
        private static final int NON_VALUE = -1;

        public int maxProfit(int[] prices) {
            int sumProfit = 0;
            int previousPrice = NON_VALUE;

            for (int price : prices) {
                if (previousPrice != NON_VALUE && previousPrice < price) {
                    int profit = price - previousPrice;
                    sumProfit += profit;
                }

                previousPrice = price;
            }

            return sumProfit;
        }
    }
}
