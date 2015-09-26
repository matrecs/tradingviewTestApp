/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.messages;

/**
 *
 * @author midikko
 */
public class GetFileRequest extends Message{
    private static final long serialVersionUID = 1271201506734918899L;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GetFileRequest{" + "id=" + id + '}';
    }
    
}
