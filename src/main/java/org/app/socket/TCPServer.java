package org.app.socket;


import org.app.model.GameModel;
import org.app.model.User;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class TCPServer extends Thread {
    private int port;
    private Selector selector = null;

    private ServerSocketChannel serverSocketChannel = null;

    private ServerSocket serverSocket = null;

    private Logger logger = null;

    private GameModel gameModel = null;

    public TCPServer(int port, Logger logger, GameModel gameModel) throws IOException {
        this.port = port;
        this.logger = logger;

        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocket = serverSocketChannel.socket();
        this.serverSocket.bind(new InetSocketAddress("localhost", port));
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketChannel.register(selector, serverSocketChannel.validOps(),null);

        this.gameModel = gameModel;
    }

    @Override
    public void run() {
        this.logger.info("Ready to serve requests!");
        try {
            List<SocketChannel> temp = new ArrayList<>();

            Boolean flag = true;
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> i = selectedKeys.iterator();

                while (i.hasNext()) {
                    SelectionKey key = i.next();

                    if (key.isAcceptable()) {
                        SocketChannel client = this.serverSocketChannel.accept();
                        client.configureBlocking(false);
                        SelectionKey selectionKey = client.register(selector, SelectionKey.OP_READ);
                        User user = new User(selectionKey, null, 0);
                        this.gameModel.addUser(user);
                        this.logger.info("Accepted new request!");

                    }
                    i.remove();

                }
            }
        } catch (IOException exception) {
            this.logger.severe(exception.getMessage());
        } finally {
            this.logger.info("Server is stopped!");
        }
    }
}
