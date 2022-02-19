package com.leetcode;

public class JumpGameOneProblem {
    public static void main(String[] args) {
        System.out.println(new Solution().canJump(new int[]{3, 2, 1, 0, 4}));
    }

    /**
     * [2, 3, 1, 1, 4]
     * 1 проход
     * вес 2, позиция 0, текущая максимальная длина пути max (0, 0 + 2) == 2
     * 2 проход
     * вес 3, позиция 1, текущая максимальная длина пути max (2, 1 + 3) == 4
     * 3 проход
     * вес 1, позиция 2, текущая максимальная длина пути max (4, 2 + 1) == 4
     * 4 проход
     * вес 1, позиция 3, текущая максимальная длина пути max (4, 3 + 1) == 4
     * 5 проход
     * вес 4, позиция 4, текущая максимальная длина пути max (4, 4 + 4) == 8
     * мы достигли после элемента
     * <p>
     * [3, 2, 1, 0, 4]
     * 1 проход
     * вес 3, позиция 0, текущая максимальная длина пути max (0, 0 + 3) == 3
     * 2 проход
     * вес 2, позиция 1, текущая максимальная длина пути max (3, 1 + 2) == 3
     * 3 проход
     * вес 1, позиция 2, текущая максимальная длина пути max (3, 2 + 1) == 3
     * 4 проход
     * вес 0, позиция 3, текущая максимальная длина пути max (3, 3 + 0) == 3
     * 5 проход
     * невозможен так как максимальная позиция до которой дошли 3, а текущая = 4
     * прыгнуть нельзя
     */
    static class Solution {
        public boolean canJump(int[] nums) {
            int maxDistance = 0;
            for (int currentIndex = 0; currentIndex < nums.length; currentIndex++) {
                if (currentIndex > maxDistance) {
                    return false;
                }

                maxDistance = Math.max(maxDistance, currentIndex + nums[currentIndex]);
            }

            return true;
        }
    }
}
