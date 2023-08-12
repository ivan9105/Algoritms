package com.algoritms.strings;

public class TwoStringIsOneModificationStep {
    public static void main(String[] args) {
        checkAndPrint("bale", "pale");
        checkAndPrint("apple", "aple");
        checkAndPrint("ballse", "balls");
        checkAndPrint("balllllse", "balls");
        checkAndPrint("goops", "balls");
    }

    private static void checkAndPrint(String first, String second) {
        System.out.printf("Is one edit step between '%s' and '%s': %s %n", first, second, check(first, second));
    }

    private static boolean check(String first, String second) {
        if (first == null || second == null) {
            return false;
        }

        // check length
        if (Math.abs(first.length() - second.length()) > 1) {
            return false;
        }

        if (first.equals(second)) {
            return false;
        }

        var longest = first.length() > second.length() ? first : second;
        var shortest = second.length() < first.length() ? second : first;

        var longestPointer = 0;
        var shortestPointer = 0;
        var hasModification = false;
        var differentLength = longest.length() != shortest.length();

        while (longestPointer < longest.length() && shortestPointer < shortest.length()) {
            char ch = longest.charAt(longestPointer);
            char otherCh = shortest.charAt(shortestPointer);

            if (ch != otherCh) {
                if (hasModification) {
                    return false;
                }

                hasModification = true;

                longestPointer++;
                if (!differentLength) {
                    shortestPointer++;
                }
            } else {
                shortestPointer++;
                longestPointer++;
            }
        }

        return true;
    }

}
