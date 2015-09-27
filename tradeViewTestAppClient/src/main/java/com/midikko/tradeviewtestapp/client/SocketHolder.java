/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.domain.DownloadState;
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
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
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
            String hash = computeHash(Paths.get("files/" + file.getFilename()));
            if (hash.equalsIgnoreCase(file.getHash())) {
                System.out.println("hashsum is ok");
            } else {
                System.out.println("hashsum is wrong");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void partitionReading(FileInfo file) {
        try {
            int partitionCount = (int) (file.getByteSize() / PARTITION_SIZE + 1);
            Path tempDirectory = Paths.get(".temp", file.getFilename());
            tempDirectory = Files.createDirectories(tempDirectory);
            DownloadState state = new DownloadState(file.getFilename());
            state.setBytes(file.getByteSize());
            state.setCurrentPartition(0);
            state.setTotalPartitions(partitionCount);
            state.setHash(file.getHash());

            for (int i = 1; i <= partitionCount; i++) {
                FileOutputStream fos;
                try {
                    byte[] mybytearray = new byte[PARTITION_SIZE];
                    InputStream is = socket.getInputStream();
                    fos = new FileOutputStream(tempDirectory + "/partition." + i);
                    try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                        int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                        bos.write(mybytearray, 0, bytesRead);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                }
                state.setCurrentPartition(i);
                updateDownloadState(state);
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
        rebuildFile(file.getFilename());
    }

    public void rebuildFile(String filename) {
        try {
            Files.deleteIfExists(Paths.get("files/" + filename));
            FileOutputStream fos = new FileOutputStream("files/" + filename, true);
            Path tempDirectory = Paths.get(".temp", filename);
            try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                Comparator<Path> byPartitionNum = (e1, e2) -> Integer.compare(
                        Integer.parseInt(e1.getFileName().toString().split("\\.")[1]), Integer.parseInt(e2.getFileName().toString().split("\\.")[1]));
                Files.walk(tempDirectory)
                        .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().matches("partition.*"))
                        .sorted(byPartitionNum)
                        .forEach(filePath -> {
                            //too bad... replace by matcher
                            try {
                                byte[] mybytearray = new byte[PARTITION_SIZE];
                                try (FileInputStream fin = new FileInputStream(filePath.toFile())) {
                                    int bytesRead = fin.read(mybytearray, 0, mybytearray.length);
                                    bos.write(mybytearray, 0, bytesRead);
                                }
                                Files.deleteIfExists(filePath);
                            } catch (FileNotFoundException ex) {
                                System.out.println("Exception while reading partition :: \n" + ex);
                            } catch (IOException ex) {
                                System.out.println("Exception while reading partition :: \n" + ex);
                            }
                        });

            }
            Path header = Paths.get(tempDirectory.toString(), "header");
            DownloadState state = getDownloadStateByFileName(filename);
            String hash = computeHash(Paths.get("files/" + filename));
            if(hash.equals(state.getHash())){
                System.out.println("Hashsum is ok");
            }else{
                System.out.println("Hashsum is wrong");
            }
            Files.deleteIfExists(header);
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

    private void updateDownloadState(DownloadState state) throws IOException {
        Path header = Paths.get(".temp", state.getFilename(), "header");
        FileOutputStream fout = new FileOutputStream(header.toFile());
        try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(state);
        }
    }

    private DownloadState getDownloadStateByFileName(String name) throws IOException {
        DownloadState state = null;
        Path header = Paths.get(".temp", name, "header");
        try (FileInputStream fin = new FileInputStream(header.toFile())) {
            ObjectInputStream ois = new ObjectInputStream(fin);
            state = (DownloadState) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }

    private String computeHash(Path path) throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("CANNOT COMPUTE HASH - SOMETHING WENTS WRONG");
        }
        try (InputStream is = Files.newInputStream(path)) {
            DigestInputStream dis = new DigestInputStream(is, md);
            /* Read stream to EOF as normal... */
        }
        byte[] digest = md.digest();
        String result = "";

        for (int i = 0; i < digest.length; i++) {
            result += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;

    }
}
