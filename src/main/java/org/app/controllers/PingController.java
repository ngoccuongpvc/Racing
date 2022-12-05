package org.app.controllers;

import org.app.model.GameModel;
import org.app.model.User;

import java.util.logging.Logger;

public class PingController extends Controller{

    public static final String __NAME__ = "PING";

    @Override
    public String handle(GameModel gameModels, Logger logger, User user, String[] args) {
        return "PONG";
    }
}
