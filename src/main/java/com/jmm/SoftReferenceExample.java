package com.jmm;

import lombok.SneakyThrows;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static com.util.GcUtil.tryToAllocateAllAvailableMemory;


public class SoftReferenceExample {
    /**
     * освобождение памяти происходит, когда JVM сильно нуждается в памяти
     * -XX:SoftRefLRUPolicyMSPerMB=2500 - после 2500 секунд будут удаляться объекты если к ним нет обращений
     *
     * -Xms512m -Xmx512m -XX:NewRatio=3 -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:gc.log
     */
    @SneakyThrows
    public static void main(String[] args) {
//        example1();
        Integer obj = 1;
        //объект obj будет удален из памяти только в случае острой нехватки, в случае weakReference удален будет сразу
        SoftReference softReference = new SoftReference(obj);
        softReference = null;
    }

    private static void example1() {
        String instance = "123323";
        SoftReference<String> softReference = new SoftReference<>(instance);
        instance = null;
        //НЕ всегда работает не все 100% если unit test написать скорее всего отловиться
        while (softReference.get() != null) {
            tryToAllocateAllAvailableMemory();
        }
    }

    private static void sample1() {
        StringBuilder builder = new StringBuilder();
        SoftReference<StringBuilder> ref = new SoftReference<>(builder);


        //НЕ всегда работает не все 100%
        List<List<Long>> idsList = new ArrayList<>();
        while (ref.get() != null) {
            ArrayList<Long> list = new ArrayList<>();
            LongStream.range(1, 100000).forEach(list::add);
            idsList.add(list);

            System.out.println(Runtime.getRuntime().freeMemory());
        }
    }
}
