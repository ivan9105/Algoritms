package com.algoritms.postfix;

import java.util.Arrays;
import java.util.Stack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class PostfixExpression {
    public static void main(String[] args) {
        var expression = "3 + 4 * 2 / (1 - 5) ^ 2".replaceAll(" ", "");
        System.out.println("Infix: " + expression + ", postfix: " + infixToPostfix(expression));
    }

    private static String infixToPostfix(String expression) {
        var stack = new Stack<SymbolType>();
        var sb = new StringBuilder();

        for (char ch : expression.toCharArray()) {
            var current = SymbolType.valueOf(ch);
            if (current == SymbolType.NUMBER) {
                sb.append(ch);
            } else if (current.isOperation) {
                var prev = stack.isEmpty() ? null : stack.peek();
                if (prev != null && prev.priority >= current.priority) {
                    while (stack.peek().priority >= current.priority && stack.peek().isOperation) {
                        sb.append(stack.pop().value);
                    }
                }
                stack.push(current);
            } else if (current == SymbolType.LEFT_BRACKET) {
                stack.push(current);
            } else if (current == SymbolType.RIGHT_BRACKET) {
                while (stack.peek().isOperation) {
                    sb.append(stack.pop().value);
                }

                if (stack.peek() == SymbolType.LEFT_BRACKET) {
                    stack.pop();
                } else {
                    throw new IllegalArgumentException("Incorrect expression");
                }
            }
        }

        while (!stack.isEmpty()) {
            sb.append(stack.pop().value);
        }

        return sb.toString();
    }


    @RequiredArgsConstructor
    @Getter
    private enum SymbolType {
        PLUS('+', 0, true),
        MINUS('-', 0, true),
        MULTIPLY('*', 1, true),
        DIVIDE('/', 1, true),
        EXPONENTIATION('^', 2, true),
        LEFT_BRACKET('(', 3, false),
        RIGHT_BRACKET(')', 3, false),
        NUMBER('W', -1, false);

        private final char value;
        private final int priority;
        private final boolean isOperation;

        public static SymbolType valueOf(char chr) {
            return Arrays.stream(values()).filter(it -> it.value == chr).findFirst().orElse(NUMBER);
        }
    }
}
