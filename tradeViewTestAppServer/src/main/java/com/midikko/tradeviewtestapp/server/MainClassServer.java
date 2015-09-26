/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.server;

import static com.midikko.tradeviewtestapp.server.MainClassServer.SERVER_PORT;

/**
 *
 * @author midikko
 */
public class MainClassServer {

    public static final int SERVER_PORT = 25565;

    public static void main(String[] args) {
        ServerSocketHolder socket = new ServerSocketHolder(SERVER_PORT);
        socket.start();

    }
}
