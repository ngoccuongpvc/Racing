package org.app.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GameModel {

    private volatile List<User> users = new ArrayList<>();

    public volatile GameBoard gameBoard = null;

    private volatile Logger logger = null;

    public GameModel(Logger logger) {
        this.logger = logger;
    }

    public synchronized List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();
        for (User user : this.getReadyUsers()) {
            usernames.add(user.username);
        }
        return usernames;
    }

    public synchronized List<Integer> getPoints() {
        List<Integer> points = new ArrayList<>();
        for (User user : this.getReadyUsers()) {
            points.add(user.point);
        }
        return points;
    }


    public synchronized Boolean addUser(User user) {
        this.logger.info("Adding new user");
        this.users.add(user);
        return true;
    }

    public synchronized List<User> getUsers() {
        return this.users;
    }

    public synchronized List<User> getReadyUsers() {
        return this.users.stream().filter(u -> u.isReady && !u.client.socket().isClosed()).collect(Collectors.toList());
    }

    public synchronized Integer numReadyUsers() {
        Integer count = 0;
        for (User user : this.users) {
            if (user.isReady && user.isActive()) {
                count += 1;
            }
        }
        return count;
    }

    public synchronized void startNewRound() {
        this.gameBoard = GameBoard.generateGame();
        for (User user : this.getReadyUsers()) {
            user.resetAnswer();
        }
    }

    public synchronized void reset() {
        for (User user : this.users) {
            try {
                user.client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.users.clear();
    }
}
