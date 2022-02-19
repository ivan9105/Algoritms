package com.leetcode;

public class JumpGameTwoProblem {
    public static void main(String[] args) {
        System.out.println(new Solution().jump(new int[]{2, 1, 3, 1, 4, 2, 3, 1, 1, 1, 2, 3}));
    }

    /**
     * https://algorithmica.org/ru/sqrt - корневая оптимизация как один из вариантов решения
     * Жадный алгоритм
     * Алгоритм по шагам
     * [2, 1, 3, 1, 4, 2, 3, 1, 1, 1, 2, 3]
     * <p>
     * <p>
     * Дано:
     * nums = массив камушков
     * <p>
     * currentIndex - текущая позиция лягушки
     * currMaxStep - текущая максимальная позиция
     * nextMaxStep - следующая максимальная позиция
     * result - кол-во шагом минимальное чтобы пройти весь путь
     * <p>
     * Подробный проход:
     * <p>
     * <p>
     * <p>
     * Прыгаем значит на первый камушек, с него можно допрыгнуть к третьему камню (вес 2), результат == 0
     * Смотрит второго камня я могу допрыгнуть ток макс до третьего (вес у него 1), текущая позиция плюс вес == 2, результат == 0
     * Смотрит на третий камень с него можно допрыгнуть на +3 (вес) соотвественно следующий максимальный шаг = 5, результат == 1
     * Идем дальше четвертый камень вес = 1, его пропускаем, результат == 1
     * пятый камень вес == 4, след максимальная позиция 8, результат == 2
     * шестой камень вес = 2, так же след максимальная позиция 7, не подходит, результат == 2
     * седьмой камень вес == 3, максимальная позиция равняеется 9, лучший вариант, результат == 3
     * идем до 9 камня, вес == 1 выбора нет, результат = 4
     * идем до 10 камня там вес 2, результат 5
     * идем дальше остался посл камень, результат 6
     */
    static class Solution {
        public int jump(int[] nums) {
            int length = nums.length;
            int currentIndex = 0;
            int currMaxStep = 0;
            int nextMaxStep = 0;
            int result = 0;

            while (currentIndex < length) {
                System.out.printf("Cur Index: %d, cur item: %d, cur max step: %d, next max step: %d, res: %d, %n",
                        currentIndex, nums[currentIndex], currMaxStep, nextMaxStep, result);
                if (currMaxStep >= length - 1) {
                    return result;
                }

                while (currentIndex <= currMaxStep) {
                    nextMaxStep = Math.max(currentIndex + nums[currentIndex], nextMaxStep);
                    if (nextMaxStep >= length - 1) {
                        return result + 1;
                    }
                    currentIndex++;
                    System.out.printf("Inner Cur Index: %d, cur item: %d, cur max step: %d, next max step: %d, res: %d, %n",
                            currentIndex, nums[currentIndex], currMaxStep, nextMaxStep, result);
                }
                result++;
                currMaxStep = nextMaxStep;
                nextMaxStep = 0;
            }

            return result;
        }
    }
}
