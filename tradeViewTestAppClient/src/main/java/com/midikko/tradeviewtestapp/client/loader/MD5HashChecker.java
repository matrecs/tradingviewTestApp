/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.loader;

import com.midikko.tradeviewtestapp.domain.FileInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Вспомогательный класс обеспечивающий получение и сверку хеш-сумм файлов
 * @author midikko
 */
public class MD5HashChecker {
    
    private MessageDigest md;

    public MD5HashChecker() {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("CANNOT COMPUTE HASH - SOMETHING WENTS WRONG");
        }
    }

    private String computeHash(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            new DigestInputStream(is, md);
            /* Read stream to EOF as normal... */
        }
        byte[] digest = md.digest();
        String result = "";

        for (int i = 0; i < digest.length; i++) {
            result += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;

    }

    /**
     * Проверка соответствия хеш-сумм двух файлов.
     * @param pathToFile Path соответствующий файлу полученному от сервера
     * @param file объект FileInfo проверяемого файла, полученный от сервера 
     * @return true - если хеш сумма верна, false если хешсуммы различаются
     * @throws IOException
     */
    public boolean checkHashSum(Path pathToFile,FileInfo file) throws IOException {
        String hash = computeHash(Paths.get(pathToFile.toString()));
        return hash.equals(file.getHash());
    }
}
