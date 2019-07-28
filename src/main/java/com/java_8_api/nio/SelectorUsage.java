package com.java_8_api.nio;

import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static java.net.InetAddress.getLocalHost;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.*;
import static java.nio.channels.Selector.open;

public class SelectorUsage {
    public static void main(String[] args) {
        SelectorUsage executor = new SelectorUsage();
        executor.execute();
    }

    @SneakyThrows
    private void execute() {
        Thread serverThread = new Thread(() -> new SelectorServer().start());
        serverThread.start();

        sleep(5000);

        Thread clientThread = new Thread(() -> new SelectorClient().run());
        clientThread.start();

        serverThread.join();
        clientThread.join();
    }
}

class SelectorClient {
    private static final String[] MESSAGES = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight"};

    @SneakyThrows
    void run() {
        InetAddress localhost = getLocalHost();
        InetSocketAddress serverAddress = new InetSocketAddress(localhost, SelectorServer.PORT);

        try (SocketChannel channel = SocketChannel.open(serverAddress)) {
            System.out.println(format("Try to connect to %s:%s", serverAddress.getHostName(), serverAddress.getPort()));
            ByteBuffer writeBuffer = allocate(1024);
            ByteBuffer readBuffer = allocate(SelectorServer.SONG.getBytes().length);

            for (String msg : MESSAGES) {
                writeBuffer.put(msg.getBytes());
                writeBuffer.flip();
                int writtenBytesSize = channel.write(writeBuffer);
                System.out.println(format("Sending Message...: %s...: %d", msg, writtenBytesSize));
                writeBuffer.clear();
            }

            while (true) {
                int readBytesSize = channel.read(readBuffer);
                if (readBytesSize != 0) {
                    readBuffer.flip();
                }

                while (readBuffer.hasRemaining()) {
                    //вывод
                    System.out.print((char) readBuffer.get());
                }
                readBuffer.clear();

                try {
                    writeBuffer.put("*exit*".getBytes());
                    writeBuffer.flip();
                    channel.write(writeBuffer);
                } catch (Exception ignore) {
                }
                sleep(1000L);
            }
        }
    }
}

class SelectorServer {
    static final Integer PORT = 9999;

    private Selector selector;

    @SneakyThrows
    void start() {
        InetAddress localhost = getLocalHost();
        selector = open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        ServerSocket socket = channel.socket();
        InetSocketAddress address = new InetSocketAddress(localhost, PORT);
        socket.bind(address);

        //обработка в неблокирующем потоке
        channel.configureBlocking(false);

        //возвращает множество операций поддерживаемых нашим каналом {@link SelectionKey}
        int validOps = channel.validOps();
        channel.register(selector, validOps, null);

        while (true) {
            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    processAccept(channel);
                } else if (key.isWritable()) {
                    processWrite(key);
                } else if (key.isReadable()) {
                    processRead(key);
                }
                keyIterator.remove();
                sleep(1000L);
            }
        }
    }

    @SneakyThrows
    private void processAccept(ServerSocketChannel channel) {
        SocketChannel clientChannel = channel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, OP_READ | OP_WRITE | OP_CONNECT);

        System.out.println(format("Client '%s' register", clientChannel.getLocalAddress().toString()));
    }

    @SneakyThrows
    private void processWrite(SelectionKey key) {
        if (key.isReadable()) {
            processRead(key);
        }

        SocketChannel clientChannel = (SocketChannel) key.channel();
        if (!clientChannel.isOpen()) {
            return;
        }
        byte[] responseData = SONG.getBytes();
        ByteBuffer buffer = allocate(responseData.length);
        buffer.put(responseData);
        buffer.flip();
        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }

    @SneakyThrows
    private void processRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = allocate(SONG.getBytes().length);
        clientChannel.read(buffer);
        String message = new String(buffer.array()).trim();
        if (message.length() > 0) {
            if (message.equalsIgnoreCase("*exit*")) {
                clientChannel.close();
            }
        }
    }

    static final String SONG = "Потрясениям и праздникам-нет \n" +
            "Горизонтам и праздникам-нет\n" +
            "Вдохновениям и праздникам-нет,нет,нет,нет\n" +
            "         Безрыбье в золотой полынье\n" +
            "         Вездесущность мышиной возни\n" +
            "         Злые сумерки бессмертного дня\n" +
            "\n" +
            "                          ДОЛГАЯ СЧАСТЛИВАЯ ЖИЗНЬ\n" +
            "                          ТАКАЯ ДОЛГАЯ СЧАСТЛИВАЯ ЖИЗНЬ\n" +
            "                           ОТНЫНЕ ДОЛГАЯ СЧАСТЛИВАЯ ЖИЗНЬ\n" +
            "                                       КАЖДОМУ ИЗ НАС\n" +
            "                                       КАЖДОМУ ИЗ НАС\n" +
            "\n" +
            "Беспощадные глубины морщин\n" +
            "Марианские впадины глаз\n" +
            "Марсианские хроники нас,нас,нас\n" +
            "         Посреди одинаковых стен\n" +
            "         В гробовых отдалённых домах\n" +
            "         В непроглядной ледяной тишине \n" +
            "\n" +
            "-*-*-*-*-*-*-*-*\n" +
            "\n" +
            "Искушениям и праздникам-нет\n" +
            "Преступлениям и праздникам-нет \n" +
            "Исключениям и праздникам-нет,нет,нет.\n" +
            "         На семи продувных сквозняках\n" +
            "         По болотам,по пустыням,степям\n" +
            "         По сугробам,по грязи,по земле\n";
}

