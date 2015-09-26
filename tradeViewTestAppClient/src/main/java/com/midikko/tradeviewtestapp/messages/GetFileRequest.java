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
public class GetFileRequest extends Message {

    private static final long serialVersionUID = 1271201506734918899L;

    public GetFileRequest(String name) {
        this.name = name;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GetFileRequest{" + "name=" + name + '}';
    }
}
