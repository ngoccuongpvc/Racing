package org.app.socket;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler {
    private Socket connection = null;

    private Logger logger;

    public ConnectionHandler(Socket connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    private Writer getWriter() {
        Writer writer = null;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            this.connection.getOutputStream(), "ASCII"
                    )
            );
        } catch (IOException e) {
            this.logger.info(e.getMessage());
        }
        return writer;
    }

    private Reader getReader() {
        Reader reader = null;
        try {
            reader = new InputStreamReader(
                    new BufferedInputStream(
                          this.connection.getInputStream()
                    )
            );
        } catch (IOException e) {
            this.logger.info(e.getMessage());
        }
        return reader;
    }

    public String read() {
        Reader reader = this.getReader();

        if (reader == null) {
            return null;
        }

        String requestMessage = null;
        try {
            if (!connection.isClosed()) {
                StringBuffer requestBuffer = new StringBuffer();
                this.logger.info("Reading input...");
                while (true) {
                    int c = reader.read();
                    if (c == 0 || c == -1) {
                        break;
                    }
                    requestBuffer.append((char) c);
                }
                requestMessage = requestBuffer.toString();
                this.logger.info("Received " + requestMessage);
            }
        } catch (IOException exception) {
            this.logger.warning(exception.getMessage());
        }
        return requestMessage;
    }

    public void write(String msg) {
        Writer writer = this.getWriter();
        if (writer == null) {
            this.logger.info("Can not get writer");
            return;
        }

        try {
            this.logger.info("Writing " + msg);
            if (!connection.isClosed()) {
                writer.write(msg);
                writer.flush();
            }
        } catch (IOException exception) {
            this.logger.warning(exception.getMessage());
        }
    }
}
