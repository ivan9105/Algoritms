package com.algoritms.bits;

/**
 * Даны два 32-разрядных числа N и ми две позиции битов i и j. Напишите
 * метод для вставки M в N так, чтобы число м занимало позицию с бита j по бит
 * i. Предполагается, что j и i имеют такие значения, что число м гарантированно
 * поместится в этот промежуток. Скажем, для м = 10011 можно считать,
 * что j и i разделены как минимум 5 битами. Комбинация вида j = з и i = 2
 * невозможна, так как число м не поместится между битом 3 и битом 2.
 * При.мер:
 * Ввод: N 10000000000, м 10011, i 2, j 6
 * Вывод: N 10001001100
 */
public class PartiallyBitInsertion {
    public static void main(String[] args) {
        //10000000000
        var source = 1024;
        //10011
        var suffix = 19;
        var from = 2;
        var to = 6;

        int result = updateBits(source, suffix, from, to);
        System.out.printf("Partially bits insertion: source %s, suffix: %s, from: %s, to: %s, result: %s %n",
                source, suffix, from, to, result);
    }

    private static int updateBits(int source, int suffix, int from, int end) {
//TODO
        return source;
    }
}
