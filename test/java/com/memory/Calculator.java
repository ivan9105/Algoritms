package com.memory;

import org.junit.Test;

public class Calculator {

    @Test
    public void objectSize() {
        //int i = 1 (4 байта - 2^32)

        //Integer i = 1 (Заголовок объекта + Память для примитивных типов + Память для ссылочных типов
        // + Смещение/выравнивание - размер объекта должен быть кратным 8 байтам)
        //Размер заголовка (8 байт (32бит), 16 байт (64 бита))
        //Заголовок содержит (Маркировочное слово, hashcode, garbadge collection information, )

//TODO https://habr.com/ru/post/134102/
    }

    @Test
    public void arraySizeWithPrimitives() {
        //float[500][14761][2]
        //500*14 761*2*4(float) = 59,044 mb по идее, но если посм с помощью eclipse memory analyzer 206,662016 mb
        //350% overhead

        //TODO https://habr.com/ru/post/142409/#:~:text=%D0%92%D1%81%D1%8F%D0%BA%D0%B8%D0%B9%20Java%2D%D0%BE%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%20%D0%B8%D0%BC%D0%B5%D0%B5%D1%82%20%D0%BE%D0%B2%D0%B5%D1%80%D1%85%D0%B5%D0%B4,%D0%B1%D0%B0%D0%B9%D1%82%D0%B0%20(%D0%B4%D0%BB%D1%8F%20%D1%85%D1%80%D0%B0%D0%BD%D0%B5%D0%BD%D0%B8%D1%8F%20%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%80%D0%B0)%3B
    }

    @Test
    public void arraySizeWithObjects() {
        //TODO сам поковыряй
    }

}
