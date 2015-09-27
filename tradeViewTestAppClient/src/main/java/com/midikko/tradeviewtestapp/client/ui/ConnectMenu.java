/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.midikko.tradeviewtestapp.client.ui;

import com.midikko.tradeviewtestapp.client.SocketHolder;

/**
 *
 * @author midikko
 */
public class ConnectMenu extends Thread {

    @Override
    public void run() {
        System.out.println("Client is ready to be used");
        System.out.println("Please enter host and port as according to the sample \"host:port\"");
        String hostPort = System.console().readLine();
        String[] inputs = hostPort.split(":");
        SocketHolder socketHolder = new SocketHolder(inputs[0], Integer.parseInt(inputs[1]));
        System.out.println("Successfully connected to " + hostPort);
        new MainMenu().start();
    }

}
