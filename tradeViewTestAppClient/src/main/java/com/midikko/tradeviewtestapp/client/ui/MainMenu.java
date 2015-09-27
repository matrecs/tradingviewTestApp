/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.ui;

import com.midikko.tradeviewtestapp.client.SocketHolder;
import com.midikko.tradeviewtestapp.domain.FileInfo;
import com.midikko.tradeviewtestapp.messages.CloseInteraction;
import com.midikko.tradeviewtestapp.messages.GetFileRequest;
import com.midikko.tradeviewtestapp.messages.GetFileResponse;
import com.midikko.tradeviewtestapp.messages.GetFilesListRequest;
import com.midikko.tradeviewtestapp.messages.GetFilesListResponse;

/**
 *
 * @author midikko
 */
public class MainMenu extends Thread {

    @Override
    public void run() {
        //TODO change all this sout to printf
        //TODO think about ui interfaces?..

        System.out.println("Select number from menu below:");
        System.out.println("1. Download new file");
        System.out.println("2. Check unfinished downloads");
        System.out.println("0. Exit");
        int selected = Integer.parseInt(System.console().readLine());
        switch (selected) {
            case 1:
                new DownloadNewFileMenu().start();
                break;
            case 2:
                new UnfinishedDownloadsMenu().start();
                break;
            case 0:
                SocketHolder.getInstance().close();
                break;
            default:
                throw new AssertionError();
        }
    }

}
