package org.app.socket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConnectionManager {
    private volatile List<Socket> connections = new ArrayList<>();

    private Logger logger = null;
    public ConnectionManager(Logger logger) {
        this.logger = logger;
    }
    public synchronized void addConnection(Socket connection) {
        this.logger.info("Adding new connection to connection pool");
        this.connections.add(connection);
        this.logger.info("Added new connection");
    }

    public synchronized List<Socket> getConnections() {
        return this.connections;
    }
}
