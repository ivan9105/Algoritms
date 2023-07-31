package com.algoritms.strings;

public class ReplaceAllSpacesOnUrlencodedSymbol {
    /**
     * Пример:
     * Ввод: "Mr John Smith   "
     * Вывод: "Mr%20John%20Smith"
     */
    public static void main(String[] args) {
        //заполняем с конца и двигаемся с права налево
        var input = "Mr John Smith  ";
        System.out.println("Convert input " + input + " to: " + convert(input));
    }

    private static String convert(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }

        input = "Mr John Smith  ".trim();
        var arr = input.toCharArray();

        int spaceCount = 0;
        for (char ch : arr) {
            if (ch == ' ') {
                spaceCount++;
            }
        }

        if (spaceCount == 0) {
            return input;
        }

        var length = input.length();
        var newLength = length + 2 * spaceCount;

        var newArr = new char[newLength];

        for (int index = length - 1; index >= 0; index--) {
            if (arr[index] == ' ') {
                newArr[newLength - 1] = '0';
                newArr[newLength - 2] = '2';
                newArr[newLength - 3] = '%';
                newLength = newLength - 3;
            } else {
                newArr[newLength - 1] = arr[index];
                newLength--;
            }
        }

        return new String(newArr);
    }
}
