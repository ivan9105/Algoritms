package com.leetcode;

public class BestTimeToBuyAndSellStockFirstProblem {
    public static void main(String[] args) {
        System.out.println(new SimpleSolution().maxProfit(new int[]{7, 6, 4, 3, 1}));
    }

    /**
     * достаточно одного прохода
     * берем минимальный ценник с начала цикла кешируем
     * макс профит считаем так, отнимаем цену от минимального ценникв каждый раз и берем max
     *
     * в итоге за один проход получаем результат
     */
    static class SimpleSolution {
        public int maxProfit(int[] prices) {
            int minBuy = Integer.MAX_VALUE;
            int maxProfit = 0;
            for (int price : prices) {
                minBuy = Math.min(minBuy, price);
                maxProfit = Math.max(maxProfit, price - minBuy);
            }
            return maxProfit;
        }
    }
}
