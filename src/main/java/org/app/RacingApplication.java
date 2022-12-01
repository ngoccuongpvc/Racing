package org.app;

import org.app.socket.TCPServer;

import java.io.IOException;
import java.util.logging.Logger;


public class RacingApplication {
    static Logger logger = Logger.getLogger("root");
    static int APPLICATION_PORT = 6969;

    public static void main(String[] args) {
        logger.info(String.format("Starting TCP Server at port %d", APPLICATION_PORT));
        TCPServer server = null;
        try {
            server = new TCPServer(APPLICATION_PORT, logger);
        } catch (IOException exception) {
            logger.severe(exception.getMessage());
            return;
        }
        logger.info("Successfully!");

        logger.info("Ready to serve requests!");
        server.start();
        logger.info("Server is stopped!");

    }
}