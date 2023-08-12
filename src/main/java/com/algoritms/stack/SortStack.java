package com.algoritms.stack;

import java.util.Stack;

public class SortStack {
    public static void main(String[] args) {

    }

    //вызывается после добавления элемента в стек можно даже обычный стек переопределить
    private static void sort(Stack<Integer> stack) {
        var sorted = new Stack<Integer>();

        var unsortedItem = stack.pop();
        while (!stack.isEmpty() && unsortedItem < stack.peek()) {
            sorted.push(stack.pop());
        }

        sorted.push(unsortedItem);

        while (!stack.isEmpty()) {
            sorted.push(stack.pop());
        }
    }
}
