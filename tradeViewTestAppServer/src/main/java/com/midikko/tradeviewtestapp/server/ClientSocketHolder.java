/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.server;

import com.midikko.tradeviewtestapp.messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author midikko
 */
public class ClientSocketHolder {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientSocketHolder(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Failed to set client io streams :: \n" + ex);
        }
    }
    
    public boolean sendMessage(Message message){
        try {
            outputStream.writeObject(message);
        } catch (IOException ex) {
            System.out.println("Error while sending message");
            return false;
        }
        return true;
    }
    
    public Message readMessage(){
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
    
}
