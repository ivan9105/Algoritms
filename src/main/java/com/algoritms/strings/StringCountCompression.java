package com.algoritms.strings;

public class StringCountCompression {
    public static void main(String[] args) {
        var input = "ааЬсссссааа";
        System.out.println("The input: " + input + ", after compression: " + compress(input));
    }

    private static String compress(String value) {
        if (value == null) {
            return value;
        }

        int length = value.length();
        if (length == 0) {
            return value;
        }

        value = value.trim();

        var sb = new StringBuilder();
        var count = 0;
        for (int index = 0; index < length; index++) {
            var ch = value.charAt(index);
            if (index < length - 1 && ch != value.charAt(index + 1)) { // след. символ != предыдущему символу
                sb.append(ch).append(count + 1);
                count = 0;
            } else if (index == length - 1) { // след. символа нет
                sb.append(ch).append(count + 1);
                count = 0;
            } else { //след. символ такой же как и пред.
                count++;
            }
        }

        return sb.toString();
    }
}
