/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.messages.Message;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midikko
 */
public class SocketHolder {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public SocketHolder(Socket socket) {
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            initializeFilesSystem();
        } catch (IOException ex) {
            System.out.println("Failed to set client io streams :: \n" + ex);
        }
    }

    public boolean sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException ex) {
            System.out.println("Error while sending message");
            return false;
        }
        return true;
    }

    public Message readMessage() {
        Message message = null;
        try {
            message = (Message) inputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("error while read message :: \n" + ex);
        }
        return message;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void readFile() {
        System.out.println("Begin reading file");
        byte[] mybytearray = new byte[1024];
        FileOutputStream fos;
        try {
            InputStream is = socket.getInputStream();
            fos = new FileOutputStream("files/file.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int bytesRead = is.read(mybytearray, 0, mybytearray.length);
            bos.write(mybytearray, 0, bytesRead);
            bos.close();
            System.out.println(" reading file finished");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initializeFilesSystem() throws IOException {
        Path path = Paths.get("files"); // it's only an object
        if(!Files.exists(path)){
            System.out.println("Filefolder not exist! create one.");
            Files.createDirectory(path);
            System.out.print("  Folder Created.");
        }
    }
}
