package com.leetcode;


import lombok.Getter;
import lombok.Setter;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode(int x) { val = x; }
 * }
 */
public class TwoNumberProblem {

    public static void main(String[] args) {
        var listNode1 = new ListNode(2);
        listNode1.next = new ListNode(4);
        listNode1.next.next = new ListNode(3);

        var listNode2 = new ListNode(5);
        listNode2.next = new ListNode(6);
        listNode2.next.next = new ListNode(4);

        new TwoNumberProblem().addTwoNumbers(listNode1, listNode2);
    }

    /**
     * Example 1:
     * Input: l1 = [2,4,3], l2 = [5,6,4]
     * Output: [7,0,8]
     * Explanation: 342 + 465 = 807.
     * Example 2:
     *
     * Input: l1 = [0], l2 = [0]
     * Output: [0]
     * Example 3:
     *
     * Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
     * Output: [8,9,9,9,0,0,0,1]
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // результирующая node
        var result = new ListNode(0);
        // храним остаток
        var remain = 0;
        // временные переменые требуются для прохода по node-ам, чтобы не перетирать ссылки
        var tmp1 = l1;
        var tmp2 = l2;
        var current = result;

        //проход до тех пор пока есть ссылка на след элемент
        while (tmp1 != null || tmp2 != null) {
            var x = tmp1 != null ? tmp1.val : 0;
            var y = tmp2 != null ? tmp2.val : 0;
            var sum = x + y + remain;
            remain = sum / 10; //переносим остаток, если число больше 9
            var currentValue = sum % 10;
            current.next = new ListNode(currentValue);
            current = current.next;
            if (tmp1 != null) tmp1 = tmp1.next;
            if (tmp2 != null) tmp2 = tmp2.next;
        }

        if (remain > 0) {
            current.next = new ListNode(remain);
        }

        //по факту вернется node которая следует за инициализированной node == 0, а с ней и все остальные ссылки
        //если бы возвращали просто result в таком случае на конце всегда был бы лишний 0
        return result.next;
    }


    @Getter
    @Setter
    public static class ListNode {
        private int val;
        private ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}

