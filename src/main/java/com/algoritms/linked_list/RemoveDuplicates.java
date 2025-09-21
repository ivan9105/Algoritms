package com.algoritms.linked_list;

import java.util.HashSet;

public class RemoveDuplicates {
    public static void main(String[] args) {
        var head = new ListNode(4);
        head.next = new ListNode(5);
        head.next.next = new ListNode(5);
        head.next.next.next = new ListNode(4);

        removeDuplicates(head);
        printList(head);
    }

    private static void removeDuplicates(ListNode head) {
        var cache = new HashSet<Integer>();

        ListNode prev = null;
        while (head != null) {
            if (cache.contains(head.val)) {
                prev.next = head.next;
            } else {
                cache.add(head.val);
                prev = head;
            }
            head = head.next;
        }
    }

    private static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            if (head.next != null) {
                System.out.print("-> ");
            }
            head = head.next;
        }
    }

    private static class ListNode {
        private int val;
        private ListNode next;
        public ListNode(int x) {
            val = x;
            next = null;
        }
    }
}
