/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.domain;

import java.io.Serializable;

/**
 *
 * @author midikko
 */
public class FileInfo implements Serializable{
    private static final long serialVersionUID = -6078891588911784282L;
    
    private String filename;
    private long byteSize;
    private String hash;
    private long partsCount;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getByteSize() {
        return byteSize;
    }

    public void setByteSize(long byteSize) {
        this.byteSize = byteSize;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    
    
}
