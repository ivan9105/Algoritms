package com.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.checkerframework.checker.units.qual.A;

public class JumpGameThreeProblem {

    public static void main(String[] args) {
        System.out.println(new Solution().canReach(new int[]{4, 2, 3, 0, 3, 1, 2}, 5));
    }

    /**
     * Рекомендуется использование Breadth-first search (BFS) - Поиск в ширину
     * <p>
     * Задача:
     * понять можем ли мы добраться до элемента со значением = 0
     */
    static class Solution {
        private static final int PROCESSED = -1;

        /**
         * Обходим все элементы начиная слева направо, уже пройденные элементы помечаем значением -1 (PROCESSED)
         *
         * [4, 2, 3, 0, 3, 1, 2] start = 5
         * Первая итерация первый элемент позиция 5
         * левый индекс = 4
         * правый индекс = 6
         * помечаем индекс 5 пройденным [4, 2, 3, 0, 3, -1, 2]
         * очередь [HEAD - 6, LAST - 4]
         *
         * Вторая итерация левый индекс с конца очереди
         * левый индекс по отношению к 4 = 2
         * правый индекс skip
         * помечаем индекс 6 пройденным [4, 2, 3, 0, -1, -1, 2]
         * очередь [HEAD - 1, LAST - 6]
         *
         * Третья итерация берем 6 индекс
         * левый индекс по отношению к 6 = 4 позиция - она пройдена
         * правый идекс выходит лимит поэтому skip
         * помечаем индекс 6 пройденным [4, 2, 3, 0, -1, -1, -1]
         * очередь [HEAD/LAST - 1]
         *
         * след итерация берем 1
         * левый индекс -1 skip
         * правый индекс = 3 добавляем в очередь
         * помечаем индекс 6 пройденным [4, -1, 3, 0, -1, -1, -1]
         * очередь [HEAD/LAST - 3]
         *
         * след итерация и элемент с индексом 3 == 0 мы его нашли
         *
         */
        public boolean canReach(int[] arr, int start) {
            Queue<Integer> queue = new LinkedList<>();
            queue.add(start);

            while (!queue.isEmpty()) {
                int currentPosition = queue.poll();

                if (arr[currentPosition] == 0) {
                    return true;
                }

                int leftPosition = currentPosition - arr[currentPosition];
                int rightPosition = currentPosition + arr[currentPosition];

                arr[currentPosition] = PROCESSED;

                if (leftPosition >= 0 && arr[leftPosition] != PROCESSED) {
                    queue.add(leftPosition);
                }

                if (rightPosition < arr.length && arr[rightPosition] != PROCESSED) {
                    queue.add(rightPosition);
                }
            }

            return false;
        }

    }
}
