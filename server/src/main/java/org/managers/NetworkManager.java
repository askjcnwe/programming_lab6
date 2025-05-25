package org.managers;

import org.commands.Container;
import java.io.*;
import java.net.*;

/**
 * Класс для обмена данными с клиентом
 */
public class NetworkManager {
    private DatagramSocket socket;
    private int port;
    private int timeout;
    private byte[] buffer = new byte[16384]; // Увеличенный размер буфера
    private SocketAddress lastClientAddress;

    public NetworkManager(int port, int timeout) {
        this.port = port;
        this.timeout = timeout;
    }

    /**
     * Метод для настройки подключения
     * @return результат
     */
    public boolean init() {
        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(timeout);
            System.out.println("Server started on port " + port);
            return true;
        } catch (SocketException e) {
            System.err.println("Failed to initialize server socket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Метод для отправки данных на клиент
     * @param data данные
     * @return статус отправки
     */
    public boolean sendData(byte[] data) {
        try {
            if (data == null || lastClientAddress == null) {
                System.err.println("Cannot send data: " + 
                    (data == null ? "data is null" : "no client address available"));
                return false;
            }
            DatagramPacket response = new DatagramPacket(
                    data,
                    data.length,
                    lastClientAddress
            );
            socket.send(response);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to send data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Метод для получения данных с клиента
     * @param len максимальная длина принимаемых данных
     * @return полученные данные
     */
    public byte[] receiveData(int len) {
        try {
            // Используем минимальное значение между запрошенным размером и размером буфера
            DatagramPacket packet = new DatagramPacket(buffer, Math.min(len, buffer.length));
            socket.receive(packet);
            
            // Сохраняем адрес отправителя для ответа
            lastClientAddress = packet.getSocketAddress();
            
            // Копируем только полученные данные
            byte[] data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());
            return data;
        } catch (SocketTimeoutException e) {
            // Таймаут это нормальная ситуация, не нужно логировать
            return null;
        } catch (IOException e) {
            System.err.println("Failed to receive data: " + e.getMessage());
            return null;
        }
    }

    /**
     * Метод для сериализации данных
     * @param obj Объект
     * @return сериализованные данные
     */
    public static byte[] serializer(Object obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Метод для сериализации полученных данных
     * @param bytes данные
     * @return десериализованный объект
     */
    public static Container deserialize(byte[] bytes) {
        if (bytes == null) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (Container) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization failed: " + e.getMessage());
            return null;
        }
    }
}