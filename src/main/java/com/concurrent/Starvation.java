package com.concurrent;

import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;

import static java.util.stream.IntStream.range;

public class Starvation {
    public static void main(String[] args) {
        range(0, 10).forEach(iteration -> new Thread(() -> new Worker().work()).start());
    }

    /**
     * Работает только один поток, а другие ожидают
     */
    public static class Worker {
        @SneakyThrows
        public synchronized void work() {
            String name = Thread.currentThread().getName();
            String fileName = name + ".txt";

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("Thread " + name + " wrote this mesasge");

            while (true) {
//                wait(1000);
                System.out.println(name + " is working");
            }
        }
    }

}
