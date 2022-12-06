package org.app.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.time.Instant;

public class User {

    public SelectionKey key;
    public SocketChannel client;

    public String username;

    public Integer point;

    public Boolean isReady = false;

    public Integer answer = null;

    public Instant timestamp = null;

    public Integer consecutiveFailedAnswer = 0;

    public Boolean isEliminated = false;

    public User(SelectionKey key, String username, Integer point) {
        this.key = key;
        this.client = (SocketChannel) key.channel();
        this.username = username;
        this.point = point;
    }

    public String read() {
        if (this.key.isReadable()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String msg = null;

            try {
                this.client.read(buffer);
            } catch (IOException e) {
                //do nothing
            }
            msg = new String(buffer.array()).trim();
            if (msg.length() > 0) {
                return msg;
            }
            return null;
        } else {
            return null;
        }
    }

    public Boolean write(String msg) {
        if (!this.isActive()) {
            return false;
        }
        try {
            this.client.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            // do nothing
        }
        return true;
    }

    public void resetAnswer() {
        this.answer = null;
        this.timestamp = null;
    }

    public Boolean isActive() {
        return !this.client.socket().isClosed();
    }
}
