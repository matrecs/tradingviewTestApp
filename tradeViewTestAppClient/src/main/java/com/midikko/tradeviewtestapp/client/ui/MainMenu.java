/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.ui;

import com.midikko.tradeviewtestapp.client.SocketHolder;
import com.midikko.tradeviewtestapp.client.loader.FileLoader;
import com.midikko.tradeviewtestapp.domain.FileInfo;
import com.midikko.tradeviewtestapp.messages.CloseInteraction;
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
 * Класс описывающий поток обеспечивающий взаимодействие с пользователем.
 * Основное меню обеспечивающее переход в
 *
 * @see DownloadNewFileMenu и закрытие клиента.
 * @author midikko
 */
public class MainMenu extends Thread {

    @Override
    public void run() {
        System.out.println("Client is ready to be used");
        System.out.println("Please enter host and port as according to the sample \"host:port\"");
        String hostPort = System.console().readLine();
        String[] inputs = hostPort.split(":");
        SocketHolder socketHolder = new SocketHolder(inputs[0], Integer.parseInt(inputs[1]));
        System.out.println("Successfully connected to " + hostPort);
        GetFilesListRequest message = new GetFilesListRequest();
        socketHolder.sendMessage(message);
        GetFilesListResponse fileListResponse = (GetFilesListResponse) socketHolder.readMessage();
        System.out.println("server at this time contains the following files:");
        int selector = 0;
        for (FileInfo file : fileListResponse.getFiles()) {
            System.out.println(selector++ + ". " + file.getFilename());
        }
        System.out.println("Please select the number of file you wish to download");
        int selectedFileNumber;
        while (true) {
            try {
                selectedFileNumber = Integer.parseInt(System.console().readLine());
                if (selectedFileNumber <= fileListResponse.getFiles().length && selectedFileNumber >= 0) {
                    break;
                }
            } catch (NumberFormatException ex) {
            }
        }
        if (selectedFileNumber > fileListResponse.getFiles().length || selectedFileNumber < 0) {
            return;
        }
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
                    }

                }
                break;
                case 2:
                    directory = Paths.get(FileLoader.DEFAULT_FILE_DIRECTORY);
                    break;
                case 3:
                    return;
                default:
            }
        }
        socketHolder.sendMessage(new GetFileRequest(fileToDownload.getFilename()));
        GetFileResponse getFileResponse = (GetFileResponse) socketHolder.readMessage();
        if (getFileResponse.getStatus() == 1) {
            socketHolder.readFile(directory, fileToDownload);
            System.out.println("Download finished.");
        } else {
            System.out.println("Selected file is not availiable now.");
        }
        socketHolder.sendMessage(new CloseInteraction());
    }
}
