/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.messages.GetFilesRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midikko
 */
public class MainClassClient {

    public static final int SERVER_PORT = 25565;

    public static void main(String[] args) {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            System.out.println("Client on the way!");
            Socket socket = new Socket("localHost", SERVER_PORT);
            System.out.println("Connected");
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            GetFilesRequest message = new GetFilesRequest();
            System.out.println("Object to be written = " + message);
            outputStream.writeObject(message);
            System.out.println("Read response :: " + inputStream.readObject());
            
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainClassClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
            }
        }

    }
}
