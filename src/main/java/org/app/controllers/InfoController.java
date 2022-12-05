package org.app.controllers;

import org.app.model.GameModel;
import org.app.model.User;

import java.util.List;
import java.util.logging.Logger;

public class InfoController extends Controller {
    public static final String __NAME__ = "INFO";

    @Override
    public String handle(GameModel gameModels, Logger loggers, User user, String[] args) throws Exception {
        List<String> usernames = gameModels.getUsernames();

        return String.format("%s %s\0", InfoController.__NAME__, String.join(",", usernames));
    }
}
