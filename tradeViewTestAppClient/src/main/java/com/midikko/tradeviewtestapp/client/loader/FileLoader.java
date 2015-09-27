/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.loader;

import com.midikko.tradeviewtestapp.domain.FileInfo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс обеспечивающий получение файлов с сервера.
 *
 * @author midikko
 */
public class FileLoader {

    public static final String DEFAULT_FILE_DIRECTORY = "files/";
    private final MD5HashChecker hashChecker;
    public static final int PARTITION_SIZE = 1024 * 100;

    private InputStream stream;

    /**
     * Конструктор, принимающий на вход поток сокета.
     *
     * @param stream поток сокета с которого будет производится чтение.
     * @throws IOException
     */
    public FileLoader(InputStream stream) throws IOException {
        this.stream = stream;
        initializeFilesSystem();
        hashChecker = new MD5HashChecker();
    }

    private void initializeFilesSystem() throws IOException {
        Path filesFolder = Paths.get(DEFAULT_FILE_DIRECTORY); // it's only an object
        if (!Files.exists(filesFolder)) {
            System.out.println("Files folder not exist! create one.");
            Files.createDirectory(filesFolder);
            System.out.print("Folder Created.");
        }
    }

    /**
     * Метод начинающий считывание указанного файла в указанную директорию
     *
     * @param path - Path директории в которую будет записан файл
     * @param file - файл который будет получен с сервера
     */
    public void readFile(Path path, FileInfo file) {
        int partitionCount = (int) (file.getByteSize() / PARTITION_SIZE + 1);
        Path newFilePath = Paths.get(path.toString(), file.getFilename());
        try (RandomAccessFile raf = new RandomAccessFile(newFilePath.toString(), "rw")) {
            for (int i = 1; i <= partitionCount; i++) {
                byte[] mybytearray = new byte[PARTITION_SIZE];
                stream.read(mybytearray, 0, mybytearray.length);
                raf.seek(raf.length());
                raf.write(mybytearray);
            }
            System.out.println("HashSum status ::" + hashChecker.checkHashSum(path, file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
