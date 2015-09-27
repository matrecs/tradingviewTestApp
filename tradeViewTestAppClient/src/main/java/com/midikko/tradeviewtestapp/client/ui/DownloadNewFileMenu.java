/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.ui;

import com.midikko.tradeviewtestapp.client.SocketHolder;
import com.midikko.tradeviewtestapp.client.loader.FileLoader;
import com.midikko.tradeviewtestapp.domain.FileInfo;
import com.midikko.tradeviewtestapp.messages.GetFileRequest;
import com.midikko.tradeviewtestapp.messages.GetFileResponse;
import com.midikko.tradeviewtestapp.messages.GetFilesListRequest;
import com.midikko.tradeviewtestapp.messages.GetFilesListResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midikko
 */
public class DownloadNewFileMenu extends Thread {

    @Override
    public void run() {
        SocketHolder socketHolder = SocketHolder.getInstance();
        GetFilesListRequest message = new GetFilesListRequest();
        System.out.println("Object to be written = " + message);
        socketHolder.sendMessage(message);
        GetFilesListResponse fileListResponse = (GetFilesListResponse) socketHolder.readMessage();
        System.out.println("server at this time contains the following files:");
        int selector = 0;
        for (FileInfo file : fileListResponse.getFiles()) {
            System.out.println(selector++ + ". " + file.getFilename());
        }
        System.out.println("Please select the number of file you wish to download");
        int selectedFileNumber = Integer.parseInt(System.console().readLine());
        FileInfo fileToDownload = fileListResponse.getFiles()[selectedFileNumber];

        System.out.println("Please, insert full path to directory where file will be placed (files/ by default)");
        String path = System.console().readLine();
        Path directory;
        if (!path.isEmpty()) {
            directory = Paths.get(path);
        } else {
            directory = Paths.get(FileLoader.DEFAULT_FILE_DIRECTORY);
        }
        if (!Files.exists(directory)) {
            System.out.println("Directory with path " + path + " not exist. Create ?");
            System.out.println("1. Yes");
            System.out.println("2. Download to default dir");
            System.out.println("3. Cancel download");
            int selected = Integer.parseInt(System.console().readLine());
            switch (selected) {
                case 1: {
                    try {
                        Files.createDirectories(directory);
                    } catch (IOException ex) {
                        Logger.getLogger(DownloadNewFileMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                break;
                case 2:
                    directory = Paths.get(FileLoader.DEFAULT_FILE_DIRECTORY);
                    break;
                case 3:
                    new MainMenu().start();
                    return;
                default:
            }
        }
        socketHolder.sendMessage(new GetFileRequest(fileToDownload.getFilename()));
        GetFileResponse getFileResponse = (GetFileResponse) socketHolder.readMessage();
        if (getFileResponse.getStatus() == 1) {
            socketHolder.readFile(directory, fileToDownload);
            System.out.println("Download finished.");
        }else{
            System.out.println("Selected file is not availiable now.");
        }
        
        System.out.println("1. Download another file.");
        System.out.println("2 . Return to mine menu.");
        int selected = Integer.parseInt(System.console().readLine());
        switch (selected) {
            case 1:
                new DownloadNewFileMenu().start();
                break;
            case 2:
                new MainMenu().start();
                break;
            default:
                throw new AssertionError();
        }
    }

}
