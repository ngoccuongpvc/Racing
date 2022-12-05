package org.app.controllers;

import org.app.socket.ConnectionHandler;
import org.app.socket.ConnectionManager;

import java.net.Socket;
import java.util.logging.Logger;

public class GameController extends Thread {

    private Logger logger = null;

    private ConnectionManager connectionManager = null;
    public GameController(ConnectionManager connectionManager, Logger logger) {
        this.connectionManager = connectionManager;
        this.logger = logger;
    }

    @Override
    public void run() {
        while (true) {
            for (Socket connection : this.connectionManager.getConnections()) {
                ConnectionHandler connectionHandler = new ConnectionHandler(connection, logger);
                String msg = connectionHandler.read();
                connectionHandler.write(msg);
            }
        }
    }
}
