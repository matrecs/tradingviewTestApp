/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.messages;

import com.midikko.tradeviewtestapp.domain.FileInfo;
import java.util.Arrays;

/**
 * Класс, описывающий ответ на запрос о получении списка файлов.
 * @see GetFilesListRequest
 * @author midikko
 */
public class GetFilesListResponse extends Message {

    private static final long serialVersionUID = 6683479949287415979L;
    private FileInfo[] files;

    /**
     * Метод на получение текущего списка файлов доступных для передачи.
     * @return массив объектов типа FileInfo.
     */
    public FileInfo[] getFiles() {
        return files;
    }

    /**
     * Метод на установку списка файлов. 
     * @param files Массив объектов типа FileInfo
     */
    public void setFiles(FileInfo[] files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "GetFilesListResponse{" + "files=" + Arrays.toString(files) + '}';
    }

}
