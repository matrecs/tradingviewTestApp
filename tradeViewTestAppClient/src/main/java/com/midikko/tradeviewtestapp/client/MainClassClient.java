/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client;

import com.midikko.tradeviewtestapp.client.ui.MainMenu;


/**
 *
 * @author midikko
 */
public class MainClassClient {

    public static final int SERVER_PORT = 25569;

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.start();
    }
}
