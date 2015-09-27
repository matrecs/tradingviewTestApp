/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.messages;

/**
 * Класс, описывающий ответ на запрос о перелаче файла.
 * @see GetFileRequest
 * @author midikko
 */
public class GetFileResponse extends Message {

    private static final long serialVersionUID = -4564176865603416902L;
    private int status;

    /**
     * Конструктор принимающий статус операции.
     * @param status 1 - файл найден, начинается передача, 0 - запрашиваемый файл файл не найден.
     */
    public GetFileResponse(int status) {
        this.status = status;
    }

    /**
     * Метод на получение статуса операции.
     * @return status 1 - файл найден, начинается передача, 0 - запрашиваемый файл файл не найден.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Метод на установку статуса операции.
     * @param status 1 - файл найден, начинается передача, 0 - запрашиваемый файл файл не найден.
     */
    public void setStatus(int status) {
        this.status = status;
    }

}
