package org.app.controllers;

import org.app.model.GameModel;

import java.util.List;
import java.util.logging.Logger;

public class InfoController extends Controller {
    public static final String __NAME__ = "INFO";

    @Override
    public String handle(GameModel gameModels, Logger loggers, String[] args) throws Exception {
        List<String> usernames = gameModels.getUsernames();

        return InfoController.__NAME__ + " " + String.join(",", usernames);
    }
}
