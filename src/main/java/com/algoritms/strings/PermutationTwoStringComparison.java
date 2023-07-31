package com.algoritms.strings;

public class PermutationTwoStringComparison {
    public static void main(String[] args) {
        var first = "agbbc";
        var second = "bbagc";

        System.out.println("The first string: " + first + " is permutation of second string: " + second + ", is: " + isPermutation(first, second));
    }

    private static boolean isPermutation(String first, String second) {
        if (first == null || second == null) {
            return false;
        }

        if (first.length() != second.length()) {
            return false;
        }

        //unicode 128 symbols
        var letters = new int[128];

        for (int index = 0; index < first.length(); index++) {
            int charIndex = first.charAt(index);
            letters[charIndex]++;
        }

        for (int index = 0; index < second.length(); index++) {
            int charIndex = second.charAt(index);
            if (letters[charIndex]-- < 0) {
                return false;
            }
        }

        return true;
    }
}
