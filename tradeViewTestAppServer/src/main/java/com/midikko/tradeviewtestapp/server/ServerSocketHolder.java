/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.server;

import com.midikko.tradeviewtestapp.domain.FileInfo;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс-обертка для серверного сокета.
 * @author midikko
 */
public class ServerSocketHolder extends Thread {

    public static Map<String, FileInfo> files = new HashMap<>();

    private ServerSocket socket;

    /**
     * Конструктор сокета, принимающий на вход номер порта.
     * @param port
     */
    public ServerSocketHolder(int port) {
        try {
            initializeFilesSystem();
            socket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("Server socket is offline :: \n" + ex);
        }
        System.out.println("Server socket is online :: " + socket);
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                System.out.println("Wait for client");
                new ClientGreeter(new ClientSocketHolder(socket.accept())).start();

            } catch (IOException ex) {
                Logger.getLogger(ServerSocketHolder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void initializeFilesSystem() throws IOException {

        Path path = Paths.get("files"); // it's only an object
        System.out.println("path :: " + path);
        if (!Files.exists(path)) {
            System.out.println("Filefolder not exist! create one.");
            Files.createDirectory(path);
            System.out.print("  Folder Created.");
        }
        Files.walk(Paths.get("files")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                try {
                    files.put(filePath.getFileName().toString(), new FileInfo(filePath));
                } catch (IOException ex) {
                    Logger.getLogger(ServerSocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
