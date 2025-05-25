package org.managers;

import org.utility.ExecutionResponse;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

/**
 * Класс для обмена данными с сервером
 */
public class NetworkManager {
    private DatagramChannel channel;
    private InetAddress host;
    private int port;
    private int timeout;
    private SocketAddress serverAddress;

    public NetworkManager(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Метод для установки стандартных параметров подключения
     * @throws IOException исключение
     */
    private void defInit() throws IOException {
        host = InetAddress.getByName("localhost");
        port = 17534;
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        serverAddress = new InetSocketAddress(host, port);
    }

    /**
     * Метод для настройки подключения
     * @param args аргументы
     * @return результат инициализации
     */
    public boolean init(String[] args) {
        try {
            if (args.length == 0) {
                defInit();
            } else {
                String filename = args[0];
                if (filename != null && !filename.isEmpty()) {
                    try (Scanner fileReader = new Scanner(new File(filename))) {
                        String[] net = new String[2];
                        byte counter = 0;
                        for (int i = 0; i < net.length && fileReader.hasNextLine(); i++) {
                            counter++;
                            net[i] = fileReader.nextLine();
                        }
                        if (counter != 2) {
                            defInit();
                        } else {
                            host = InetAddress.getByName(net[0]);
                            port = Integer.parseInt(net[1]);
                            channel = DatagramChannel.open();
                            channel.configureBlocking(false);
                            serverAddress = new InetSocketAddress(host, port);
                        }
                    }
                } else {
                    defInit();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Initialization failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Отправить данные на сервер
     * @param arr массив данных
     * @return результат отправки
     */
    public boolean sendData(byte[] arr) {
        if (arr == null || serverAddress == null) return false;
        
        try {
            ByteBuffer buffer = ByteBuffer.wrap(arr);
            int bytesSent = channel.send(buffer, serverAddress);
            return bytesSent == arr.length;
        } catch (IOException e) {
            System.err.println("Failed to send data: " + e.getMessage());
            return false;
        }
    }

    /**
     * мемтод для сериализации данных
     * @param obj Объект
     * @return сериализованные данные
     */
    public static byte[] serializeData(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * метод для сериализации данных
     * @param bytes сериализованные данные
     * @return результат десериализации
     */
    public static ExecutionResponse deserializeData(byte[] bytes) {
        if (bytes == null) return new ExecutionResponse(false, "Нет ответа от сервера");
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (ExecutionResponse) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization failed: " + e.getMessage());
            return new ExecutionResponse(false, "Ошибка десериализации: " + e.getMessage());
        }
    }

    /**
     * Метод для получения данных от сервера
     * @param length размер буффера
     * @return массив полученных данных
     */
    public byte[] receiveData(int length) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(length);
            long startTime = System.currentTimeMillis();
            SocketAddress sender;

            // Ждем данные с таймаутом
            while ((System.currentTimeMillis() - startTime) < timeout) {
                sender = channel.receive(buffer);
                if (sender != null) {
                    buffer.flip();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    return data;
                }
                Thread.sleep(10);
            }
            return null;
        } catch (IOException | InterruptedException e) {
            System.err.println("Receive failed: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return null;
        }
    }
}