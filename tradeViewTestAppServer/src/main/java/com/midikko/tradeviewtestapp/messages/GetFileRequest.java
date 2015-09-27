/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.messages;

/**
 * Класс описывающий сообщение-запрос на передачу файла.
 *
 * @author midikko
 */
public class GetFileRequest extends Message {

    private static final long serialVersionUID = 1271201506734918899L;
    private String name;

    /**
     * Получение имени запрашиваемого файла.
     * @return
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GetFileRequest{" + "name=" + name + '}';
    }

}
