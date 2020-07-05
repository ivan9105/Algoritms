package com.java_8_api.nio;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.ByteBuffer.allocate;
import static java.nio.charset.StandardCharsets.UTF_8;

public class BufferUsage {
    private static final String FILE_NAME = "nio_buffer.txt";
    private static final Integer READ_BUFFER_SIZE = 10;
    private static final String SONG = "Свято место не бывает в пустоте\n" +
            "Лишним телом заложили котлован\n" +
            "Красной тряпкой обернули катафалк\n" +
            "Бравой песней заглушили злое горе\n" +
            "\n" +
            "     Ведь солдатами не рождаются\n" +
            "            Солдатами умирают\n" +
            "\n" +
            "Свято место не бывает без врагов\n" +
            "Полированным прикладом наугад\n" +
            "В непростреленной шинели напролом\n" +
            "Бравым маршем заглушив зубовный скрежет\n" +
            "\n" +
            "     Ведь солдатами не рождаются\n" +
            "            Солдатами умирают\n" +
            "\n" +
            "Свято место не бывает в чистоте\n" +
            "Смрадным ветром затопили берега\n" +
            "Гнойным прахом напитали чернозём\n" +
            "Табаком закоротив хмельные ноздри\n" +
            " \n" +
            "     Ведь солдатами не рождаются\n" +
            "            Солдатами умирают\n" +
            "\n" +
            "Свято место не бывает без греха\n" +
            "Закуси девичьим криком — благодать!\n" +
            "Пригубить медовой браги да поблевать\n" +
            "Красным флагом утерев густые слезы\n" +
            "\n" +
            "     Ведь солдатами не рождаются\n" +
            "            Солдатами умирают\n";

    public static void main(String[] args) {
        BufferUsage executor = new BufferUsage();
        executor.execute();
    }

    private void execute() {
        writeToFile();
        readFromFile();
    }

    @SneakyThrows
    private void writeToFile() {
        //channel non blocking os
        try (FileChannel channel = new FileOutputStream(FILE_NAME).getChannel()) {
            byte[] bytes = SONG.getBytes(UTF_8);
            ByteBuffer buffer = allocate(bytes.length); //allocate buffer capacity
            buffer.put(bytes);
            buffer.flip(); //set channel to read mode
            channel.write(buffer);
        }
    }

    @SneakyThrows
    private void readFromFile() {
        try (FileChannel channel = new FileInputStream(FILE_NAME).getChannel()) {
            ByteBuffer buffer = allocate(READ_BUFFER_SIZE);
            while (channel.read(buffer) > 0) {
                buffer.flip();

                //пока есть непрочитанные байты в буфере
                while (buffer.hasRemaining()) {
                    //вывод
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
            }
        }
    }

    @SneakyThrows
    private void readFromSpecificPosition() {
        try (FileChannel channel = new FileInputStream(FILE_NAME).getChannel()) {
            //TODO channel set position записать например 10 первых строк, после остановиться записать 10 след и прочее
        }
    }
}



