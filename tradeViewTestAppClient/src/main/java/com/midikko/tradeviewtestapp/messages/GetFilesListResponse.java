/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.messages;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author midikko
 */
public class GetFilesListResponse extends Message{

    private static final long serialVersionUID = 6683479949287415979L;

    private String[] names;
    private int[] ids;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "GetFilesResponse{" + "names=" + Arrays.toString(names) + ", ids=" + Arrays.toString(ids) + '}';
    }

}
