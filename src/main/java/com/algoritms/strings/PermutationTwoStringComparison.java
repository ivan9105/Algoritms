package com.algoritms.strings;

public class PermutationTwoStringComparison {
    public static void main(String[] args) {
        var first = "agbbc";
        var second = "bbagc";

        System.out.println("The first string: " + first + " is permutation of second string: " + second + ", is: " + isPermutation(first, second));
    }

    // формируем массив не пустых элементов - симмвол - порядковый номер, если во второй строке мы не находим данные по порядковому номеру значит это не перестановка
    private static boolean isPermutation(String first, String second) {
        if (first == null || second == null) {
            return false;
        }

        if (first.length() != second.length()) {
            return false;
        }

        //unicode 128 symbols
        var letters = new int[128];
        var firstArr = first.toCharArray();
        var secondArr = second.toCharArray();

        for (var index = 0; index < firstArr.length; index++) {
            letters[first.charAt(index)]++;
        }

        for (var index = 0; index < secondArr.length; index++) {
            if (letters[second.charAt(index)]-- == 0) {
                return false;
            }
        }

        return true;
    }
}
