/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.domain.FileInfo;
import com.midikko.tradeviewtestapp.messages.Message;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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

    public static final int PARTITION_SIZE = 1024;
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

    public void readFile(FileInfo file) {
        System.out.println("Begin reading file");
        if (file.getByteSize() < 1024) {
            standartReading(file);
        } else {
            partitionReading(file);
        }
    }

    private void standartReading(FileInfo file) {
        byte[] mybytearray = new byte[PARTITION_SIZE];
        FileOutputStream fos;
        try {
            InputStream is = socket.getInputStream();
            fos = new FileOutputStream("files/" + file.getFilename());
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

    private void partitionReading(FileInfo file) {
        try {
            int partitionCount = (int) (file.getByteSize() / PARTITION_SIZE);
            Path tempDirectory = Paths.get(".temp", file.getFilename());
            tempDirectory = Files.createDirectories(tempDirectory);
//            Path header = Paths.get(tempDirectory.toString(), "header");
//            Files.createFile(header);
//            
//            FileOutputStream fout = new FileOutputStream(header.toFile());
//            ObjectOutputStream oos = new ObjectOutputStream(fout);
//            oos.writeObject(file);
//            oos.close();

            for (int i = 0; i <= partitionCount; i++) {
                byte[] mybytearray = new byte[PARTITION_SIZE];
                FileOutputStream fos;
                try {
                    InputStream is = socket.getInputStream();
                    fos = new FileOutputStream(tempDirectory + "/partition." + i);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    bos.write(mybytearray, 0, bytesRead);
                    bos.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            FileOutputStream fos = new FileOutputStream("files/" + file.getFilename(), true);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            Files.walk(tempDirectory).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    System.out.println(filePath);
                    try {
                        byte[] mybytearray = new byte[PARTITION_SIZE];
                        FileInputStream fin = new FileInputStream(filePath.toFile());
                        int bytesRead = fin.read(mybytearray, 0, mybytearray.length);
                        bos.write(mybytearray, 0, bytesRead);
                        Files.deleteIfExists(filePath);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            bos.close();
            Files.deleteIfExists(tempDirectory);
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initializeFilesSystem() throws IOException {
        Path filesFolder = Paths.get("files"); // it's only an object
        if (!Files.exists(filesFolder)) {
            System.out.println("Files folder not exist! create one.");
            Files.createDirectory(filesFolder);
            System.out.print("  Folder Created.");
        }
        Path tempFolder = Paths.get(".temp"); // it's only an object
        if (!Files.exists(tempFolder)) {
            System.out.println("Temporary folder not exist! create one.");
            Files.createDirectory(tempFolder);
            System.out.print("  Folder Created.");
        }
    }
}
