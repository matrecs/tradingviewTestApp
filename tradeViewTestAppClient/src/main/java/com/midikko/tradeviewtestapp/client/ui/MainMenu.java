/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.ui;

import com.midikko.tradeviewtestapp.client.SocketHolder;

/**
 * Класс описывающий поток обеспечивающий взаимодействие с пользователем.
 * Основное меню обеспечивающее переход в 
 * @see DownloadNewFileMenu
 * и закрытие клиента.
 * @author midikko
 */
public class MainMenu extends Thread {

    @Override
    public void run() {

        System.out.println("Select number from menu below:");
        System.out.println("1. Download new file");
        System.out.println("0. Exit");
        int selected = Integer.parseInt(System.console().readLine());
        switch (selected) {
            case 1:
                new DownloadNewFileMenu().start();
                break;
            case 0:
                SocketHolder.getInstance().close();
                break;
            default:
                throw new AssertionError();
        }
    }

}
