package org.app.socket;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler {
    private Socket connection = null;

    private Logger logger;

    ConnectionHandler(Socket connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    private Writer getWriter() throws IOException {
        Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                        this.connection.getOutputStream(), "ASCII"
                )
        );
        return writer;
    }

    private Reader getReader() throws IOException {
        Reader reader = new InputStreamReader(
                new BufferedInputStream(
                      this.connection.getInputStream()
                )
        );
        return reader;
    }

    public void handle() {
        Writer writer = null;
        Reader reader = null;
        this.logger.info("Handling connection!");
        try {
            writer = this.getWriter();
            reader = this.getReader();
        } catch (IOException exception) {
            this.logger.warning("Error while handling connection: " + exception.getMessage());
            return;
        }

        try {
            while (!connection.isClosed()) {
                StringBuffer requestBuffer = new StringBuffer();
                this.logger.info("Reading input...");
                while (true) {
                    int c = reader.read();
                    this.logger.info(String.format("Received: %d", c));

                    if (c == 0 || c == -1) {
                        break;
                    }
                    requestBuffer.append((char) c);
                }
                String requestMessage = requestBuffer.toString();
                this.logger.info("Received " + requestMessage);
                writer.write("You sent: " + requestMessage);
                writer.flush();
            }
        } catch (IOException exception) {
            this.logger.warning(exception.getMessage());
        }

    }
}
