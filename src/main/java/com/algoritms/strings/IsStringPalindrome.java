package com.algoritms.strings;

public class IsStringPalindrome {
    //Дана строка, которая может содержать произвольные символы. Проверить, является ли строка палиндромом.
    //Считаем, что строка является палиндромом, если после приведения всех букв латинского алфавита к нижнему регистру
    // и удаления всех остальных символов она читается в прямом и обратном порядке одинаково.
    public static void main(String[] args) {
        // is palindrome == TRUE
        var input = "A man, a plan, a canal: Panama";

        System.out.println("First solution: " + solutionTwo(input));
    }

    private static boolean solutionTwo(String input) {
        var processed = processString(input);
        var arr = processed.toCharArray();

        int length = processed.length();
        int half = length / 2;

        for (int i = 0; i < half; i++) {
            if (arr[i] != arr[length - 1 - i]) {
                return false;
            }
        }

        return true;
    }

    private static boolean solutionOne(String input) {
        var processed = processString(input);

        var reverseProcessed = reverseString(processed);
        return reverseProcessed.equals(processed);
    }

    private static String processString(String input) {
        return input
                .replaceAll(" ", "")
                .replaceAll(",", "")
                .replaceAll(":", "").toLowerCase();
    }

    private static String reverseString(String input) {
        var length = input.length();
        char[] arr = input.toCharArray();
        int half = length / 2;
        for (int index = 0; index < half; index++) {
            char temp = arr[index];
            arr[index] = arr[length - 1 - index];
            arr[length - 1 - index] = temp;
        }
        return new String(arr);
    }

    //TODO solution2
}
