/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.loader;

import com.midikko.tradeviewtestapp.client.SocketHolder;
import static com.midikko.tradeviewtestapp.client.SocketHolder.PARTITION_SIZE;
import com.midikko.tradeviewtestapp.domain.DownloadState;
import com.midikko.tradeviewtestapp.domain.DownloadStateManager;
import com.midikko.tradeviewtestapp.domain.FileInfo;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midikko
 */
public class FileLoader {

    public static final String DEFAULT_FILE_DIRECTORY = "files/";
    public static final String DEFAULT_TEMPORARY_FILES_DIRECTORY = ".temp";
    private final MD5HashChecker hashChecker;
    private final DownloadStateManager downloadStateManager = DownloadStateManager.INSTANCE;

    InputStream stream;

    public FileLoader(InputStream stream) throws IOException {
        this.stream = stream;
        initializeFilesSystem();
        hashChecker = new MD5HashChecker();
    }

    private void standartReading(FileInfo file) {
        byte[] mybytearray = new byte[PARTITION_SIZE];
        try {
            FileOutputStream fos = new FileOutputStream(DEFAULT_FILE_DIRECTORY + file.getFilename());
            try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                int bytesRead = stream.read(mybytearray, 0, mybytearray.length);
                bos.write(mybytearray, 0, bytesRead);
            }

            System.out.println(" reading file finished");
            System.out.println("HASH SUM STATUS :: " + hashChecker.checkHashSum(file.getFilename()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void partitionReading(FileInfo file) {
        try {
            int partitionCount = (int) (file.getByteSize() / PARTITION_SIZE + 1);
            Path tempDirectory = Paths.get(DEFAULT_TEMPORARY_FILES_DIRECTORY, file.getFilename());
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
                    fos = new FileOutputStream(tempDirectory + "/partition." + i);
                    try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                        int bytesRead = stream.read(mybytearray, 0, mybytearray.length);
                        bos.write(mybytearray, 0, bytesRead);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
                }
                state.setCurrentPartition(i);
                downloadStateManager.updateDownloadState(state);
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
        rebuildFile(file.getFilename());
    }

    public void rebuildFile(String filename) {
        try {
            Files.deleteIfExists(Paths.get(DEFAULT_FILE_DIRECTORY + filename));
            FileOutputStream mainFileStream = new FileOutputStream(DEFAULT_FILE_DIRECTORY + filename, true);
            Path tempDirectory = Paths.get(DEFAULT_TEMPORARY_FILES_DIRECTORY, filename);
            try (BufferedOutputStream bos = new BufferedOutputStream(mainFileStream)) {
                Files.walk(tempDirectory)
                        .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().matches("partition.*"))
                        .sorted((e1, e2) -> Integer.compare(
                                        Integer.parseInt(e1.getFileName().toString().split("\\.")[1]), Integer.parseInt(e2.getFileName().toString().split("\\.")[1])))
                        .forEach(filePath -> {
                            //too bad... replace by matcher
                            try {
                                byte[] mybytearray = new byte[PARTITION_SIZE];
                                try (FileInputStream partitionStream = new FileInputStream(filePath.toFile())) {
                                    int bytesRead = partitionStream.read(mybytearray, 0, mybytearray.length);
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
            System.out.println("HASH SUM STATUS :: " + hashChecker.checkHashSum(filename));
            Files.deleteIfExists(header);
            Files.deleteIfExists(tempDirectory);
        } catch (IOException ex) {
            Logger.getLogger(SocketHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeFilesSystem() throws IOException {
        Path filesFolder = Paths.get(DEFAULT_FILE_DIRECTORY); // it's only an object
        if (!Files.exists(filesFolder)) {
            System.out.println("Files folder not exist! create one.");
            Files.createDirectory(filesFolder);
            System.out.print("  Folder Created.");
        }
        Path tempFolder = Paths.get(DEFAULT_TEMPORARY_FILES_DIRECTORY); // it's only an object
        if (!Files.exists(tempFolder)) {
            System.out.println("Temporary folder not exist! create one.");
            Files.createDirectory(tempFolder);
            System.out.print("  Folder Created.");
        }
    }



    public void readFile(FileInfo file) {
        if (file.getByteSize() < PARTITION_SIZE) {
            standartReading(file);
        } else {
            partitionReading(file);
        }
    }

}
