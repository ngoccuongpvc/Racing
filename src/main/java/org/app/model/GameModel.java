package org.app.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private List<String> usernames = new ArrayList<>();

    public List<String> getUsernames() {
        return this.usernames;
    }

    public boolean addUsername(String username) {
        this.usernames.add(username);
        return true;
    }
}
