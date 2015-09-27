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

    /**
     * Порт сервера.
     */
    public static final int SERVER_PORT = 25569;

    /**
     * Размер минимального передаваемого пакета.
     */
    public static final int PARTITION_SIZE = 1024 * 100;

    public static void main(String[] args) {
        while (true) {
        System.out.println("Please enter server port or press enter to use default(25569):");
        String input = System.console().readLine();
        int port;
        if(input.isEmpty()){
            port= SERVER_PORT;
            break;
        }else{
            try {
                port = Integer.parseInt(input);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Bad input. Please Try again");
            }
        }
            
        }
        
        ServerSocketHolder socket = new ServerSocketHolder(SERVER_PORT);
        socket.start();

    }
}
