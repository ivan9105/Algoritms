package com.algoritms.linked_list;

public class LinkedListCircle {
    public static void main(String[] args) {
        var head = new ListNode(3);
        head.next = new ListNode(2);
        head.next.next = new ListNode(0);
        head.next.next.next = new ListNode(-4);
        head.next.next.next.next = head.next;

        System.out.println(hasCycle(head));
    }

    public static boolean hasCycle(ListNode head) {
        var first = head;
        var second = head;
        while (second != null && second.next != null) {
            first = first.next;
            second = second.next.next;
            if (first == second) {
                return true;
            }
        }
        return false;
    }

    public static class ListNode {
        private int val;
        private ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }
}
