/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.messages.GetFileRequest;
import com.midikko.tradeviewtestapp.messages.GetFileResponse;
import com.midikko.tradeviewtestapp.messages.GetFilesListRequest;
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

    public static final int SERVER_PORT = 25569;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            System.out.println("Client on the way!");
            socket = new Socket("localHost", SERVER_PORT);
            System.out.println("Connected");
            SocketHolder socketHolder = new SocketHolder(socket);
            GetFilesListRequest message = new GetFilesListRequest();
            System.out.println("Object to be written = " + message);
            socketHolder.sendMessage(message);
            System.out.println("Read response :: " + socketHolder.readMessage());
            socketHolder.sendMessage(new GetFileRequest(1));
            GetFileResponse response = (GetFileResponse) socketHolder.readMessage();
            System.out.println("Read response on get file :: " + response.getStatus());
            if(response.getStatus()==1){
                socketHolder.readFile();
            }
        } catch (IOException ex) {
        }finally{
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainClassClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
    }
}
