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




            //TODO
//        int i = nums.length - 2;
//        while (i >= 0 && nums[i + 1] <= nums[i]) {
//            i--;
//        }
//        if (i >= 0) {
//            int j = nums.length - 1;
//            while (nums[j] <= nums[i]) {
//                j--;
//            }
//            swap(nums, i, j);
//        }
//        reverse(nums, i + 1);
//    }
//
//    private void reverse(int[] nums, int start) {
//        int i = start, j = nums.length - 1;
//        while (i < j) {
//            swap(nums, i, j);
//            i++;
//            j--;
//        }
//    }
//
//    private void swap(int[] nums, int i, int j) {
//        int temp = nums[i];
//        nums[i] = nums[j];
//        nums[j] = temp;
//    }
        }
    }


}
