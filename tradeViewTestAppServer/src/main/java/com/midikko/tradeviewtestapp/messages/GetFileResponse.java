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
public class GetFileResponse extends Message {

    private static final long serialVersionUID = -4564176865603416902L;
    int status;

    public GetFileResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
