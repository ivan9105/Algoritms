package com.leetcode;

public class JumpGameTwoProblem {
    public static void main(String[] args) {
        System.out.println(new Solution().jump(new int[]{2, 1, 3, 1, 4, 2, 3, 1, 1, 1, 2, 3}));
    }

    /**
     * https://algorithmica.org/ru/sqrt - корневая оптимизация как один из вариантов решения
     * Жадный алгоритм
     * Движемся по камушкам и ищем максимально быстрый путь за меньшее кол-во прыжков
     * Особенность алгоритма: каждый раз прыгая на определенный камушек мы проверяем возможность с этого камушка прыгнуть как можно дальше внутренним циклом
     * Если такая возможность есть запоминаем самый длинный путь и увеличиваем на 1 кол-во прыжков
     */
    static class Solution {
        public int jump(int[] nums) {
            var length = nums.length;
            var currentIndex = 0;
            var currMaxStep = 0;
            var nextMaxStep = 0;
            var result = 0;

            while (currentIndex < length) {
                if (currMaxStep >= length - 1) {
                    return result;
                }

                while (currentIndex <= currMaxStep) {
                    nextMaxStep = Math.max(currentIndex + nums[currentIndex], nextMaxStep);
                    if (nextMaxStep >= length - 1) {
                        return result + 1;
                    }
                    currentIndex++;
                }
                result++;
                currMaxStep = nextMaxStep;
                nextMaxStep = 0;
            }

            return result;
        }
    }
}
