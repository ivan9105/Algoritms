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

    //посчитать количество пробелов, посчитать новую длину массива и заполнить, заполнение идет с конца массва
    private static String convert(String input) {
        input = input.trim();
        if (input == null || input.isEmpty()) {
            return input;
        }

        var spaceCount = 0;
        var oldArr = input.toCharArray();
        for (var ch : oldArr) {
            if (ch == ' ') {
                spaceCount++;
            }
        }
        var newLength = input.length() + spaceCount * 2;
        var newArr = new char[newLength];
        for (var index = input.length() - 1; index >= 0; index--) {
            var ch = oldArr[index];
            if (ch == ' ') {
                newArr[--newLength] = '0';
                newArr[--newLength] = '2';
                newArr[--newLength] = '%';
            } else {
                newArr[--newLength] = ch;
            }
        }

        return new String(newArr);
    }
}
