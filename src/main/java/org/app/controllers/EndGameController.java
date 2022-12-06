package org.app.controllers;

import org.app.model.GameModel;
import org.app.model.User;

import java.util.logging.Logger;

public class EndGameController extends Controller {
    public static final String __NAME__ = "END_GAME";
    public static final String END_GAME_MSG = "END_GAME";

    @Override
    public String handle(GameModel gameModels, Logger logger, User user, String[] args) throws Exception {
        return String.format("%s\0", EndGameController.END_GAME_MSG);
    }
}
