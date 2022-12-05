package org.app.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private List<String> usernames = new ArrayList<>();

    private List<Integer> points = new ArrayList<>();

    public List<String> getUsernames() {
        return this.usernames;
    }

    public List<Integer> getPoints() {
        return this.points;
    }

    public boolean addUsername(String username) {
        this.usernames.add(username);
        this.points.add(0);
        return true;
    }

}
