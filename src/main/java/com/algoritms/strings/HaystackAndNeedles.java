package com.algoritms.strings;

//Input: haystack = "hello", needle = "ll"
//Output: 2
public class HaystackAndNeedles {
    public static void main(String[] args) {
        var haystack = "heltllo";
        var needle = "ll";

        System.out.println("Needle index '" + needle + "' in haystack: " + haystack + " is " + findNeedle(haystack, needle));
    }

    private static int findNeedle(String haystack, String needle) {
        var haystackArr = haystack.toCharArray();
        var needleArr = needle.toCharArray();

        for (int index = 0; index < haystackArr.length; index++) {
            if (needleArr[0] == haystackArr[index]) {
                if (checkSubString(needleArr, haystackArr, index)) {
                    return index;
                }
            }
        }
        return -1;
    }

    private static boolean checkSubString(char[] needleArr, char[] hayStackArr, int innerIndex) {
        var needleIndex = 0;
        var needleLength = needleArr.length;
        for (int index = innerIndex; index < hayStackArr.length; index++) {
            if (needleIndex + 1 > needleLength) {
                return true;
            }

            if (hayStackArr[index] != needleArr[needleIndex++]) {
                return false;
            }
        }

        return true;
    }
}
