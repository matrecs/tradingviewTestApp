/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.server;

import com.midikko.tradeviewtestapp.domain.FileInfo;
import com.midikko.tradeviewtestapp.messages.GetFileRequest;
import com.midikko.tradeviewtestapp.messages.GetFileResponse;
import com.midikko.tradeviewtestapp.messages.GetFilesListResponse;
import com.midikko.tradeviewtestapp.messages.GetFilesListRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс обеспечивающий общение между клиентом и сервером.
 *
 * @author midikko
 */
public class ClientGreeter extends Thread {

    ClientSocketHolder client;

    /**
     * Принимает в себя холдер сокета клиента.
     *
     * @param client
     */
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
                case "GetFilesListRequest": {
                    System.out.println("Send getFiles answer");
                    GetFilesListResponse response = new GetFilesListResponse();

                    List<FileInfo> filesList = new ArrayList<>();
                    ServerSocketHolder.files.entrySet().forEach((entry) -> {
                        filesList.add(entry.getValue());
                    });
                    response.setFiles(filesList.toArray(new FileInfo[0]));
                    System.out.println(response);
                    client.sendMessage(response);
                    break;
                }
                case "GetFileRequest": {
                    GetFileRequest request = (GetFileRequest) message;
                    FileInfo file = ServerSocketHolder.files.get(request.getName());
                    if (file == null) {
                        client.sendMessage(new GetFileResponse(0));
                    } else {
                        client.sendMessage(new GetFileResponse(1));
                    }
                    client.sendFile(file.getPath());
                    break;
                }
                case "CloseInteraction": {
                    System.out.println("Close connection");
                    client.close();
                    return;
                }
                default:
                    throw new AssertionError();
            }
        }
    }

}
