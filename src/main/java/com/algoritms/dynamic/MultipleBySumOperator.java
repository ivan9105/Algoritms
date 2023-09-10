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

        var smaller = x > y ? y : x;
        var bigger = y == smaller ? x : y;
        System.out.println("The multiple of x: " + x + ", y: " + y + " is equals: " + calc(smaller, bigger));
    }

    private static int calc(int smaller, int bigger) {
        if (smaller == 0) {
            return 0;
        } else if (smaller == 1) {
            return bigger;
        }

        int temp = smaller >> 1; // divide into 2
        int halfProd = calc(temp, bigger);

        if (smaller % 2 == 0) {
            return halfProd + halfProd;
        } else {
            return halfProd + halfProd + bigger;
        }
    }
}
