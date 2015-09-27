/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.server;

import com.midikko.tradeviewtestapp.messages.Message;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * Класс-врапеер для клиентского сокета.
 * @author midikko
 */
public class ClientSocketHolder {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    /**
     * Конструктор, принимает сокет клиента.
     * @param socket сокет клиента.
     */
    public ClientSocketHolder(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Failed to set client io streams :: \n" + ex);
        }
    }

    /**
     * Отправка сообщения клиенту,
     * @param message передаваемое сообщение,
     * @return true, если сообщение успешно отправлено, false если во время отправки произошла ошибка.
     */
    public boolean sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException ex) {
            System.out.println("Error while sending message");
            return false;
        }
        return true;
    }

    /**
     * Чтение сообщения от клиенту.
     * @return Подкласс Message если успешно считано, null если во время считывания произошла ошибка.
     */
    public Message readMessage() {
        Message message = null;
        try {
            message = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("error while read message :: \n" + ex);
        }
        return message;
    }

    /**
     * Передает соответствующий файл клиенту.
     * @param path Path передаваемого файла.
     */
    public void sendFile(Path path) {
        try {
            byte[] mybytearray;
            System.out.println("Begin sending file");
            File file = path.toFile();
            try (RandomAccessFile raf = new RandomAccessFile(file, "r"); FileChannel inChannel = raf.getChannel()) {
                ByteBuffer buffer = ByteBuffer.allocate(MainClassServer.PARTITION_SIZE);
                OutputStream os = socket.getOutputStream();
                while (inChannel.read(buffer) > 0) {
                    buffer.flip();
                    mybytearray = buffer.array();
                    buffer.clear(); // do something with the data and clear/compact it.
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                }
            }
            System.out.println("sending file finished");
        } catch (IOException ex) {
            System.out.println("Error while sending file :: " + ex);
        }
    }

    /**
     * Проверка не закрыт ли сокет клиента;
     * @return true если сокет закрыт, false если сокет не закрыт.
     */
    public boolean isClosed() {
        return socket.isClosed();
    }
}
