package com.leetcode;

public class NextPermutationProblem {
    public static void main(String[] args) {
        //TODO
    }

    static class Solution {
        /**
         * Пример
         * 0125330
         * <p>
         * Шаги:
         * <p>
         * 1) Поиск наибольшего неувеличиваюшегося суффикса - 5330
         * 2) Найдем точку поворота - 2
         * 3) Найдем наименьший элемент относительно точки возрастания - 3 (позиция - крайняя слева)
         * 4) Поменяем местами элементы и получим наименьший по возрастанию префикс - 0135320
         * 5) Отсортируем получившийся суффикс  (5320) - 0130235
         * 6) Результат
         */
        public void nextPermutation(int[] nums) {
            int length = nums.length;
            int pivot;

            //TODO коммент
            for (int i = length - 1; i > 0; i--) {
                if (nums[i - 1] < nums[i]) {
                    pivot = i;
                    for (int j = pivot; j < length; j++) {
                        if (nums[j] <= nums[pivot] && nums[i - 1] < nums[j]) {
                            pivot = j;
                        }
                    }

                    int temp = nums[pivot];
                    nums[pivot] = nums[i - 1];
                    nums[i - 1] = temp;

                    //TODO
                }
            }


        }
    }
}
