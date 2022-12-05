package org.app.socket;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConnectionManager {
    private volatile List<SocketChannel> connections = new ArrayList<>();

    private Logger logger = null;
    public ConnectionManager(Logger logger) {
        this.logger = logger;
    }
    public synchronized void addConnection(SocketChannel connection) {
        this.logger.info("Adding new connection to connection pool");
        this.connections.add(connection);
        this.logger.info("Added new connection");
    }

    public synchronized List<SocketChannel> getConnections() {
        return this.connections.stream().filter(c -> !c.isConnected()).collect(Collectors.toList());
    }
}
