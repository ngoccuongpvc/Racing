package org.app.controllers;

import org.app.model.GameModel;
import org.app.model.User;

import java.util.logging.Logger;

public class JoinRoomController extends Controller {

    public static final String __NAME__ = "JOIN_ROOM";

    @Override
    public String handle(GameModel gameModels, Logger logger, User user, String[] args) throws Exception {

        if (args.length != 1) {
            throw new Exception("Invalid number of arguments");
        }

        logger.info(String.format("%s joined room", args[0]));

        user.username = args[0];
        user.isReady = true;

        return "OK\0";
    }
}
