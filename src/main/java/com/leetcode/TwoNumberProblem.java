package com.leetcode;

import java.util.List;

public class TwoNumberProblem {

    public static void main(String[] args) {

    }


    /**
     * Input: l1 = [0], l2 = [0]
     * Output: [0]
     */

    /**
     * Input: l1 = [2,4,3], l2 = [5,6,4]
     * Output: [7,0,8]
     * Explanation: 342 + 465 = 807
     */

    /**
     * Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
     * Output: [8,9,9,9,0,0,0,1]
     */

    /**
     * The number of nodes in each linked list is in the range [1, 100].
     * 0 <= Node.val <= 9
     * It is guaranteed that the list represents a number that does not have leading zeros.
     */

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        static ListNode of(List<Integer> values) {
            ListNode res = new ListNode();

            if (values == null || values.isEmpty()) {
                throw new IllegalStateException("Values list is null");
            }

            res.val = values.get(0);

            int size = values.size();

            if (size == 1) {
                return res;
            }

            int lastIndex = size - 1;

            for (int currentIndex = 0; currentIndex < size; currentIndex++) {
                if (currentIndex == lastIndex) {
                    continue;
                }


            }
        }
    }
}

