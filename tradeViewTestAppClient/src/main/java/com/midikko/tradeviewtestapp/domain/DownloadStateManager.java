/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.domain;

import com.midikko.tradeviewtestapp.client.SocketHolder;
import static com.midikko.tradeviewtestapp.client.loader.FileLoader.DEFAULT_TEMPORARY_FILES_DIRECTORY;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midikko
 */
public enum DownloadStateManager {

    INSTANCE;

    public synchronized void updateDownloadState(DownloadState state) throws IOException {
        Path header = Paths.get(DEFAULT_TEMPORARY_FILES_DIRECTORY, state.getFilename(), "header");
        FileOutputStream fout = new FileOutputStream(header.toFile());
        try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(state);
        }
    }

    public synchronized DownloadState getDownloadStateByFileName(String name) throws IOException {
        DownloadState state = null;
        Path header = Paths.get(DEFAULT_TEMPORARY_FILES_DIRECTORY, name, "header");
        try (FileInputStream fin = new FileInputStream(header.toFile())) {
            ObjectInputStream ois = new ObjectInputStream(fin);
            state = (DownloadState) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }
}
