package org.app.socket;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPServer {
    private int port;
    private ServerSocket server = null;

    private Logger logger = null;
    public TCPServer(int port, Logger logger) throws IOException {
        this.port = port;
        this.logger = logger;

        this.server = new ServerSocket(this.port);
    }

    public void start() {
        try {
            while (true) {
                Socket connection = this.server.accept();
                this.logger.info("Accepted new request!");
                try {
                    ConnectionHandler handler = new ConnectionHandler(connection, this.logger);
                    handler.handle();
                    connection.close();
                } catch (IOException ex) {
                    this.logger.warning(ex.getMessage());
                } finally {
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
        } catch (IOException exception) {
            this.logger.severe(exception.getMessage());
        }
    }
}
