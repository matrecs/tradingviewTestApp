/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс описываюший DTO для передачи информации о списке файлов клиенту.
 *
 * @author midikko
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -6078891588911784282L;

    private String filename;
    private transient Path path;
    private long byteSize;
    private String hash;

    /**
     * Конструктор, принимающий на вход Path-объект и заполняющий поля объекта
     * на его основе.
     *
     * @param path
     * @throws IOException
     */
    public FileInfo(Path path) throws IOException {
        this.path = path;
        this.filename = path.getFileName().toString();
        this.byteSize = Files.size(path);
        this.hash = computeHash();

    }

    /**
     * Метод на получение имени файла.
     *
     * @return
     */
    public String getFilename() {
        return path.getFileName().toString();
    }

    /**
     * Метод на получение длины файла в байтах
     *
     * @return длины файла в байтах
     * @throws IOException
     */
    public long getByteSize() throws IOException {
        return Files.size(path);
    }

    /**
     * Метод на получение hash-суммы файла
     *
     * @return хеш!сумма MD5
     */
    public String getHash() {
        return hash;
    }

    /**
     * Метод на получение Path
     *
     * @return
     */
    public Path getPath() {
        return path;
    }

    private String computeHash() throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error while try to get hash");
        }
        InputStream is = Files.newInputStream(path);
        byte[] dataBytes = new byte[1024];

        int nread;

        while ((nread = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }

}
