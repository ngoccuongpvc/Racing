package org.app.controllers;

import org.app.model.GameModel;
import org.app.model.User;

import java.util.logging.Logger;

public class StartGameController extends Controller{
    public static final String __NAME__ = "START_GAME";

    private static final String msg = "START_GAME\0";

    @Override
    public String handle(GameModel gameModels, Logger loggers, User user, String[] args) throws Exception {
        return StartGameController.msg;
    }
}
