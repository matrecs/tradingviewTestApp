/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midikko
 */
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -6078891588911784282L;

    private String filename;
    private transient Path path;
    private long byteSize;
    private String hash;

    public FileInfo(Path path) throws IOException {
        this.path = path;
        this.filename = path.getFileName().toString();
        this.byteSize = Files.size(path);
        this.hash = computeHash();
        
    }

    public String getFilename() {
        return path.getFileName().toString();
    }

    public long getByteSize() throws IOException {
        return Files.size(path);
    }

    public String getHash() {
        return hash;
    }

    public Path getPath() {
        return path;
    }

    private String computeHash() throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("CANNOT COMPUTE HASH - SOMETHING WENTS WRONG");
        }
        try (InputStream is = Files.newInputStream(this.path)) {
            DigestInputStream dis = new DigestInputStream(is, md);
            /* Read stream to EOF as normal... */
        }
        byte[] digest = md.digest();
        String result = "";

        for (int i = 0; i < digest.length; i++) {
            result += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;

    }

}
