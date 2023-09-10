package com.algoritms.bits;

/**
 * Даны два 32-разрядных числа N и ми две позиции битов from и to. Напишите
 * метод для вставки suffix в N так, чтобы число м занимало позицию с бита to по бит
 * from. Предполагается, что to и from имеют такие значения, что число м гарантированно
 * поместится в этот промежуток. Скажем, для м = 10011 можно считать,
 * что to и from разделены как минимум 5 битами. Комбинация вида to = з и from = 2
 * невозможна, так как число м не поместится между битом 3 и битом 2.
 * При.мер:
 * Ввод: N 10000000000, м 10011, from 2, to 6
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

    private static int updateBits(int source, int suffix, int from, int to) {
        //Последовательность из единиц
        int allOnes = ~0;
        //Единицы до позиции to, потом нули. left = 11100000
        int left = allOnes << (to + 1);
        // Единицы после позиции from. right : 00000011
        int right = ((1 << from) - 1);
        // Все единицы, нули только в позициях от 1 до to. mask 11100011
        int mask = left | right;
        /* Сбросить биты с to по from, затем поместить suffix. */
        //Сброс битов с to по from.
        int source_cleared = source & mask;
        // Переместить suffix в правильную позицию.
        int suffix_shifted = suffix << 1;
        //OR
        return source_cleared | suffix_shifted;
    }
}
