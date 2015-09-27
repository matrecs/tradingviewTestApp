/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.domain;

import java.io.Serializable;

/**
 * Класс описываюший DTO для передачи информации о списке файлов клиенту.
 *
 * @author midikko
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -6078891588911784282L;

    private String filename;
    private long byteSize;
    private String hash;

    /**
     * Метод на получение имени файла.
     *
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Метод на получение длины файла в байтах
     *
     * @return длины файла в байтах
     * @throws IOException
     */
    public long getByteSize() {
        return byteSize;
    }

    /**
     * Метод на получение hash-суммы файла
     *
     * @return хеш!сумма MD5
     */
    public String getHash() {
        return hash;
    }

}