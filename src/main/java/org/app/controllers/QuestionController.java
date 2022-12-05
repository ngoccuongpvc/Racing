package org.app.controllers;

import org.app.model.GameBoard;
import org.app.model.GameModel;
import org.app.model.User;

import java.time.Instant;
import java.util.logging.Logger;

public class QuestionController extends Controller {

    public static final String __NAME__ = "QUESTION";

    @Override
    public String handle(GameModel gameModels, Logger loggers, User user, String[] args) throws Exception {
        GameBoard gameBoard = gameModels.gameBoard;
        Integer a = gameBoard.firstNumber;
        Integer b = gameBoard.secondNumber;
        Character c = gameBoard.operator;
        Instant t = gameBoard.timestamp;
        Integer d = 30;

        String response = String.format("QUESTION %d %d %c %d %d\0", a, b, c, t.getEpochSecond(), d);
        return response;
    }
}
