/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.client.loader.FileLoader;
import com.midikko.tradeviewtestapp.domain.FileInfo;
import com.midikko.tradeviewtestapp.messages.CloseInteraction;
import com.midikko.tradeviewtestapp.messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс описывающий враппер для сокета клиента.
 * @author midikko
 */
public class SocketHolder {
    
    private static SocketHolder instanse;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private FileLoader loader;

    /**
     * Конструктор принимающий на вход хост и порт сервера.
     * @param host - хост адресс сервера к которому подключаемся
     * @param port - порт сервера к которому подключаемся
     */
    public SocketHolder(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            loader = new FileLoader(socket.getInputStream());
            instanse = this;
        } catch (IOException ex) {
            System.out.println("Failed to set client io streams :: \n" + ex);
        }
    }

    /**
     * Отправка сообщения серверу.
     * @param message передаваемое сообщение 
     * @return  true, если сообщение успешно отправлено, false если во время отправки произошла ошибка.
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
     * Чтение сообщения от сервера.
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
     * Метод начинающий считывание указанного файла в указанную директорию
     * @param path - Path директории в которую будет записан файл
     * @param file - файл который будет получен с сервера
     */
    public void readFile(Path path,FileInfo file) {
        System.out.println("Begin reading file");
        loader.readFile(path,file);
    }

    /**
     * Получение инстанса текущего сокета с сервером.
     * Не потокобезопастно.
     * @return
     */
    public static SocketHolder getInstance() {
        return instanse;
    }

//    /**
//     * Закрытие сокета
//     */
//    public void close() {
//        sendMessage(new CloseInteraction());
//        try {
//            socket.close();
//        } catch (IOException ex) {
//            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
