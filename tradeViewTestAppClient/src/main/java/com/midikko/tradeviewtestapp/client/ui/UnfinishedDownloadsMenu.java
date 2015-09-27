/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.ui;

import static com.midikko.tradeviewtestapp.client.SocketHolder.PARTITION_SIZE;
import com.midikko.tradeviewtestapp.client.loader.FileLoader;
import com.midikko.tradeviewtestapp.domain.DownloadState;
import com.midikko.tradeviewtestapp.domain.DownloadStateManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midikko
 */
public class UnfinishedDownloadsMenu extends Thread {

    DownloadStateManager manager = DownloadStateManager.INSTANCE;

    @Override
    public void run() {
        try {
            Path temporaryFolder = Paths.get(FileLoader.DEFAULT_TEMPORARY_FILES_DIRECTORY);
            List<DownloadState> unfinishedDownloads = new ArrayList<>();
            System.out.println("In this time this files is still unfinished: ");
            Files.walk(temporaryFolder)
                    .filter(path -> Files.isDirectory(path) && !path.equals(temporaryFolder))
                    .forEach(filePath -> {
                        try {
                            DownloadState state = manager.getDownloadStateByFileName(filePath.getFileName().toString());
                            unfinishedDownloads.add(state);
                            System.out.println("1. " + state.getFilename());
                        } catch (IOException ex) {
                            Logger.getLogger(UnfinishedDownloadsMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //too bad... replace by matcher
                    });
        } catch (IOException ex) {
            Logger.getLogger(UnfinishedDownloadsMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        new MainMenu().start();
//        System.out.println("1. Download another file.");
//        System.out.println("2 . Return to mine menu.");
//        int selected = Integer.parseInt(System.console().readLine());
//        switch (selected) {
//            case 1:
//                new DownloadNewFileMenu().start();
//                break;
//            case 2:
//                new MainMenu().start();
//                break;
//            default:
//                throw new AssertionError();
//        }
    }

}
