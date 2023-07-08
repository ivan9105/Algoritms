package com.algoritms.postfix;

import java.util.Arrays;
import java.util.Stack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class InfixExpression {
    public static void main(String[] args) {
        var expression = "342*15-2^/+";
        System.out.println("Postfix: " + expression + ", infix: " + postfixToInfix(expression));
    }

    private static String postfixToInfix(String expression) {
        var stack = new Stack<String>();

        for (char ch : expression.toCharArray()) {
            var current = SymbolType.valueOf(ch);
            if (current == SymbolType.NUMBER) {
                stack.push(String.valueOf(ch));
            } else {
                var firstArg = stack.pop();
                var secondArg = stack.pop();
                stack.push("(" + secondArg + current.value +
                        firstArg + ")");
            }
        }


        return stack.pop();
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