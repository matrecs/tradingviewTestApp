/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.loader;

import static com.midikko.tradeviewtestapp.client.loader.FileLoader.DEFAULT_FILE_DIRECTORY;
import com.midikko.tradeviewtestapp.domain.DownloadState;
import com.midikko.tradeviewtestapp.domain.DownloadStateManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author midikko
 */
public class MD5HashChecker {
    
    private final DownloadStateManager downloadStateManager = DownloadStateManager.INSTANCE;
    MessageDigest md;

    public MD5HashChecker() {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("CANNOT COMPUTE HASH - SOMETHING WENTS WRONG");
        }
    }

    private String computeHash(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
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

    public boolean checkHashSum(String filename) throws IOException {
        DownloadState state = downloadStateManager.getDownloadStateByFileName(filename);
        String hash = computeHash(Paths.get(DEFAULT_FILE_DIRECTORY + filename));
        return hash.equals(state.getHash());
    }
}
