/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.messages.GetFileRequest;
import com.midikko.tradeviewtestapp.messages.GetFileResponse;
import com.midikko.tradeviewtestapp.messages.GetFilesListRequest;
import com.midikko.tradeviewtestapp.messages.GetFilesListResponse;
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
            //TODO change all this sout to printf
            System.out.println("Client is ready to be used");
            System.out.println("Please enter host and port as according to the sample \"host:port\"");
            String hostPort = System.console().readLine();
            String[] inputs = hostPort.split(":");
            socket = new Socket(inputs[0], Integer.parseInt(inputs[1]));
            System.out.println("Successfully connected to " + hostPort);
            SocketHolder socketHolder = new SocketHolder(socket);
            GetFilesListRequest message = new GetFilesListRequest();
            System.out.println("Object to be written = " + message);
            socketHolder.sendMessage(message);
            GetFilesListResponse fileListResponse = (GetFilesListResponse) socketHolder.readMessage();
            System.out.println("server at this time contains the following files:");
            int selector=0;
            for(String name : fileListResponse.getNames()){
                System.out.println(selector++ +". " + name);
            }
            System.out.println("Please select the number of file you wish to download");
            int selectedFile = Integer.parseInt(System.console().readLine());
            socketHolder.sendMessage(new GetFileRequest(selectedFile));
            GetFileResponse getFileResponse = (GetFileResponse) socketHolder.readMessage();
            System.out.println("Read response on get file :: " + getFileResponse.getStatus());
            if(getFileResponse.getStatus()==1){
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
