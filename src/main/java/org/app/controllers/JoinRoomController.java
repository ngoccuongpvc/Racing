package org.app.controllers;

import org.app.model.GameModel;

import java.util.logging.Logger;

public class JoinRoomController extends Controller {

    public static final String __NAME__ = "JOIN_ROOM";

    @Override
    public String handle(GameModel gameModels, Logger loggers, String[] args) throws Exception {

        if (args.length != 1) {
            throw new Exception("Invalid number of arguments");
        }

        gameModels.addUsername(args[0]);

        return "OK";
    }
}
