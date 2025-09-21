package com.algoritms.linked_list;

public class LinkedListSort {
    public static void main(String[] args) {
        //ищем середину sorted list по аналогии с циклами только до конца fast
        //merge для обоих

        var head = new ListNode(4);
        head.next = new ListNode(2);
        head.next.next = new ListNode(1);
        head.next.next.next = new ListNode(3);

        head = sortList(head);
        printList(head);
    }

    // разделяет список не две независимых части
    // находим середину обнуляем middle элемент в нем и возвращаем middle элемент
    public static ListNode split(ListNode head) {
        var first = head;  // fast
        var second = head; // slow

        while (first != null && first.next != null) {
            first = first.next.next;
            if (first != null) {
                second = second.next;
            }
        }

        var middle = second.next;
        second.next = null;
        return middle;
    }

    public static ListNode sortList(ListNode head) {
        // список пустой, либо содержит один элемент
        if (head == null || head.next == null) {
            return head;
        }

        var middle = split(head);

        // сортируем обе части
        head = sortList(head);
        middle = sortList(middle);

        return merge(head, middle);
    }

    public static ListNode merge(ListNode first, ListNode second) {
        if (first == null) return second;
        if (second == null) return first;

        if (first.val < second.val) {
            first.next = merge(first.next, second);
            return first;
        } else {
            second.next = merge(first, second.next);
            return second;
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

    public static class ListNode {
        private int val;
        private ListNode next;

        public ListNode() {
        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
