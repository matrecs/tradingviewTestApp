/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.server;

import com.midikko.tradeviewtestapp.messages.GetFilesResponse;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author midikko
 */
public class ClientGreeter extends Thread {

    ClientSocketHolder client;

    public ClientGreeter(ClientSocketHolder client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("Client Connected");
        Object s = client.readMessage();
        System.out.println("Object class :: " + s.getClass().getSimpleName());
        System.out.println("our message :: " + s);
        switch (s.getClass().getSimpleName()) {
            case "GetFilesRequest":
                System.out.println("Send getFiles answer");
                GetFilesResponse response = new GetFilesResponse();
                List<Path> files = ServerSocketHolder.files;
                int[] ids = new int [files.size()];
                String[] names = new String[files.size()];
                ServerSocketHolder.files.forEach((file) -> {
                    int currentIndex=files.indexOf(file);
                    ids[currentIndex]=currentIndex;
                    names[currentIndex]=file.getFileName().toString();
                }
                        
                );
                response.setIds(ids);
                response.setNames(names);
                client.sendMessage(response);
                break;
            default:
                throw new AssertionError();
        }

    }

}
