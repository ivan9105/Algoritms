package com.algoritms.dynamic;

/**
 * Напишите рекурсивную функцию для умножения двух положительных целых
 * чисел без использования оператора *. Допускается использование операций
 * сложения, вычитания и поразрядного сдвига, но их количество должно быть
 * минимальным .
 */
public class MultipleBySumOperator {
    public static void main(String[] args) {
        var x = 9;
        var y = 8;

        var smaller = Math.min(x, y);
        var bigger = y == smaller ? x : y;
        System.out.println("The multiple of x: " + x + ", y: " + y + " is equals: " + calc(smaller, bigger));
    }

    private static int calc(int smaller, int bigger) {
        if (smaller == 0) {
            return 0;
        } else if (smaller == 1) {
            return bigger;
        }

        int half = smaller >> 1; // divide into 2
        int multipleHalf = calc(half, bigger); // разделить на 2 и посчитать сумму для половины, получается стек вызовов 9 + 9, 18 + 18 и так далее, т.е. уменьшение кол-ва операций

        if (smaller % 2 == 0) {
            return multipleHalf + multipleHalf;
        } else {
            return multipleHalf + multipleHalf + bigger;
        }
    }
}
