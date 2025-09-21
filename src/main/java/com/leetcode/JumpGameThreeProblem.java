package com.leetcode;

import java.util.LinkedList;
import java.util.Queue;

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
         * dfs начиная с конкретной позици, до тех пор пока очередь не пуста
         */
        public boolean canReach(int[] arr, int start) {
            var queue = new LinkedList<Integer>();
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
