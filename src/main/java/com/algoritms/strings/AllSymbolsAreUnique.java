package com.algoritms.strings;

public class AllSymbolsAreUnique {
    public static void main(String[] args) {
        checkAndPrint("abcdefgh");
        checkAndPrint("abcccdefgh");
    }

    private static void checkAndPrint(String value) {
        System.out.println("All symbols is: " + value + ", are unique: " + isAllSymbolsAreUnique(value));
    }

    private static boolean isAllSymbolsAreUnique(String value) {
        // 128 - all symbols in unicode 128 symbols range
        var exists_char = new boolean[128];

        for (int index = 0; index < value.length(); index++) {
            int charIndex = value.charAt(index);
            if (exists_char[charIndex]) {
                return false;
            }

            exists_char[charIndex] = true;
        }

        return true;
    }
}
