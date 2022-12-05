package org.app.socket;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPServer extends Thread {
    private int port;
    private ServerSocket server = null;

    private Logger logger = null;

    ConnectionManager connectionManager = null;
    public TCPServer(int port, Logger logger, ConnectionManager connectionManager) throws IOException {
        this.port = port;
        this.logger = logger;

        this.server = new ServerSocket(this.port);
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        this.logger.info("Ready to serve requests!");
        try {
            while (true) {
                Socket connection = this.server.accept();
                this.connectionManager.addConnection(connection);
                this.logger.info("Accepted new request!");
            }
        } catch (IOException exception) {
            this.logger.severe(exception.getMessage());
        } finally {
            this.logger.info("Server is stopped!");
        }
    }
}
