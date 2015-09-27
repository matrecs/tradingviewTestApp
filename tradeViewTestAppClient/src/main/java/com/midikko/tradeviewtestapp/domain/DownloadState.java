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
public class DownloadState implements Serializable {

    private static final long serialVersionUID = 532742189635593700L;
    private String filename;
    private String hash;
    private long bytes;
    private long totalPartitions;
    private int currentPartition;

    public DownloadState(String filename) {
        this.filename = filename;
    }

    
    
    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getTotalPartitions() {
        return totalPartitions;
    }

    public void setTotalPartitions(long totalPartitions) {
        this.totalPartitions = totalPartitions;
    }

    public int getCurrentPartition() {
        return currentPartition;
    }

    public void setCurrentPartition(int currentPartition) {
        this.currentPartition = currentPartition;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
