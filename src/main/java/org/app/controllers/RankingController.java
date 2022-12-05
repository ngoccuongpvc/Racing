package org.app.controllers;

import org.app.model.GameModel;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RankingController extends Controller {
    public static String __NAME__ = "RANKING";

    @Override
    public String handle(GameModel gameModels, Logger loggers, String[] args) throws Exception {
        List<String> usernames = gameModels.getUsernames();
        List<Integer> points = gameModels.getPoints();

        String usernamesStr = String.join(",", usernames);
        String pointsStr = points.stream().map(String::valueOf).collect(Collectors.joining(","));

        return RankingController.__NAME__ + " " + usernamesStr + " " + pointsStr + "\0";
    }
}
