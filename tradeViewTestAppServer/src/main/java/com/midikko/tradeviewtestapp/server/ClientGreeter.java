/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.server;

import com.midikko.tradeviewtestapp.messages.GetFileRequest;
import com.midikko.tradeviewtestapp.messages.GetFileResponse;
import com.midikko.tradeviewtestapp.messages.GetFilesListResponse;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author midikko
 */
public class ClientGreeter extends Thread {

    ClientSocketHolder client;
    List<Path> files = ServerSocketHolder.files;

    public ClientGreeter(ClientSocketHolder client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Client Connected");
        while (!client.isClosed()) {
            System.out.println("Wait for next message");
            Object message = client.readMessage();
            System.out.println("Object class :: " + message.getClass().getSimpleName());
            System.out.println("our message :: " + message);
            switch (message.getClass().getSimpleName()) {
                case "GetFilesListRequest":{
                    System.out.println("Send getFiles answer");
                    GetFilesListResponse response = new GetFilesListResponse();
                    
                    int[] ids = new int[files.size()];
                    String[] names = new String[files.size()];
                    files.forEach((file) -> {
                        int currentIndex = files.indexOf(file);
                        ids[currentIndex] = currentIndex;
                        names[currentIndex] = file.getFileName().toString();
                    }
                    );
                    response.setIds(ids);
                    response.setNames(names);
                    client.sendMessage(response);
                    break;}
                case "GetFileRequest" :{
                    GetFileRequest request = (GetFileRequest) message;
                    if(request.getId() > files.size() || request.getId() < 0){
                        client.sendMessage(new GetFileResponse(0));
                    }else{
                        client.sendMessage(new GetFileResponse(1));
                    }
                    client.sendFile(files.get(request.getId()));
                    break;
                }
                default:
                    throw new AssertionError();
            }
        }
    }

}
