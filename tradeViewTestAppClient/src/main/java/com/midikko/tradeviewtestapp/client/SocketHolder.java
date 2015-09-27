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
 *
 * @author midikko
 */
public class SocketHolder {

    private static SocketHolder instanse;
    public static final int PARTITION_SIZE = 1024;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private FileLoader loader;

    public SocketHolder(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            loader = new FileLoader(socket.getInputStream());
            SocketHolder.instanse = this;
        } catch (IOException ex) {
            System.out.println("Failed to set client io streams :: \n" + ex);
        }
    }

    public boolean sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException ex) {
            System.out.println("Error while sending message");
            return false;
        }
        return true;
    }

    public Message readMessage() {
        Message message = null;
        try {
            message = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("error while read message :: \n" + ex);
        }
        return message;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void readFile(Path path,FileInfo file) {
        System.out.println("Begin reading file");
        loader.readFile(path,file);
    }

    public static SocketHolder getInstance() {
        return instanse;
    }

    public void close() {
        sendMessage(new CloseInteraction());
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
