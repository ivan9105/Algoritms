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

    }

    /**
     * The pseudocode is as following:
     * 1. Initialize current node to dummy head of the returning list.
     * 2. Initialize carry to 0.
     * 3. Initialize p1 and p2 to head of l1 and l2 respectively.
     * 4. Loop through lists l1 and l2 until you reach both ends.
     * 5. Set x to node p1's value. If p1 has reached the end of l1, set to 0.
     * 6. Set y to node p2's value. If p2 has reached the end of l2, set to 0.
     * 7. Set sum = x + y + carry.
     * 8. Update carry = sum / 10.
     * 9. Create a new node with the digit value of (sum mod 10) and set it to current node's next, then advance current node to next.
     * 10. Advance both p1 and p2.
     * 11. Check if carry = 1, if so append a new node with digit 11 to the returning list.
     * 12. Return dummy head's next node.
     * <p>
     * Input: l1 = [0], l2 = [0]
     * Output: [0]
     * <p>
     * Input: l1 = [2,4,3], l2 = [5,6,4]
     * Output: [7,0,8]
     * Explanation: 342 + 465 = 807
     * <p>
     * Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
     * Output: [8,9,9,9,0,0,0,1]
     * <p>
     * The number of nodes in each linked list is in the range [1, 100].
     * 0 <= Node.val <= 9
     * It is guaranteed that the list represents a number that does not have leading zeros.
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // результирующая node
        ListNode result = new ListNode(0);
        // храним остаток
        int carry = 0;
        // временные переменые требуются для прохода по node-ам, чтобы не перетирать ссылки
        ListNode tmp1 = l1;
        ListNode tmp2 = l2;
        ListNode current = result;

        //проход до тех пор пока есть ссылка на след элемент
        while (tmp1 != null || tmp2 != null) {
            int x = tmp1 != null ? tmp1.val : 0;
            int y = tmp2 != null ? tmp2.val : 0;
            int sum = x + y + carry;
            carry = sum / 10;
            int currentValue = sum % 10;
            current.next = new ListNode(currentValue);
            current = current.next;
            if (tmp1 != null) tmp1 = tmp1.next;
            if (tmp2 != null) tmp2 = tmp2.next;
        }

        if (carry > 0) {
            current.next = new ListNode(carry);
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

