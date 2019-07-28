package com.garbage_collector.java_7;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Java7Garbage {
//    -XX:PermSize Минимальный изначальный размер
//    -XX:MaxPermSize Максимальный изначальный размер
//    Xms/Xmx Максимальный и минимальный размер heap-a


    public static void main(String[] args) {
        Java7Garbage sampler = new Java7Garbage();
//        sampler.firstSample();
        sampler.secondSample();

        while (true) {

        }
    }

    /**
     * можно поразвлекаться с флажками и посмотреть через visual vm
     * как себя будет вести сборщик мусора
     * -Xms512m -Xmx512m -XX:NewRatio=3 -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:gc.log
     *
     *  добавим объектов на 400 метров будет java.lang.OutOfMemoryError: Java heap space
     *  потому что они изначально в хипе валяются
     *
     *  -XX:MaxMetaspaceSize=10m ограничить meta space
     *  java.lang.OutOfMemoryError: Metadata space если ограничить
     *  одно из отличий с 7
     *
     *  visual gc
     *  -Xms512m -Xmx512m -XX:NewRatio=3 -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:gc.log
     *  добавили до 200 метров old заполнен на приблизительно 40%
     *  survivor0 практически под завязку
     *
     *  удалили до 60 метров
     *  survivor уменьшился
     *  но не eden
     *
     *  удалили до 0 метров
     *  тоже самое
     *  ну потому что сборки мусора не было
     *
     * добавили до 120 метров
     * eden почистился все в хипе
     * survivor тоже
     *
     * NewRatio
     * во сколько old generation больше чем young
     *
     * Если в Eden много мусора объекты едут в survivor
     * Если в Eden мало мусора они переезжают в Heap
     * Использовался Parallel GC
     * Тоже самое что Serial только для работы исп неск потоков STW (stop the world меньше) но памяти ест чуть больше
     * SurvivorRatio тоже можно регулировать
     *
     * https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html
     *
     * попробуем использовать -XX:+UseConcMarkSweepGC -XX:ParallelCMSThreads=3 -Xms512m -Xmx512m -XX:NewRatio=3
     * алгоритм похож на Serial и Parallel только сборка мусора young/old разделяется по времени в отдельных потоках
     *
     *
     */
    @SneakyThrows
    private void secondSample() {
        List objects = new ArrayList();
        boolean cont = true;
        String input;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to Memory Tool!");

        while (cont) {
            System.out.println(
                    "\n\nI have " + objects.size() + " objects in use, about " +
                            (objects.size() * 10) + " MB." +
                            "\nWhat would you like me to do?\n" +
                            "1. Create some objects\n" +
                            "2. Remove some objects\n" +
                            "0. Quit");

            input = in.readLine();
            if ((input != null) && (input.length() >= 1)) {
                if (input.startsWith("0")) cont = false;
                if (input.startsWith("1")) createObjects(objects);
                if (input.startsWith("2")) removeObjects(objects);
            }
        }

        System.out.println("Bye!");
    }

    private static void createObjects(List objects) {
        System.out.println("Creating objects...");
        for (int i = 0; i < 2; i++) {
            objects.add(new byte[10*1024*1024]);
        }
    }

    private static void removeObjects(List objects) {
        System.out.println("Removing objects...");
        int start = objects.size() - 1;
        int end = start - 2;
        for (int i = start; ((i >= 0) && (i > end)); i--) {
            objects.remove(i);
        }
    }

    private void firstSample() {
        //создается 5 объектов в eden
        String a = "hello";
        String b = "apple";
        String c = "banana";
        String d = "apricot";
        String e = "pear";
        //высвобождаем из 4
        a = null;
        b = null;
        c = null;
        e = null;
        //когда Eden полон сработает Copy Collection
        //d скопируется в survivor


    }
}
