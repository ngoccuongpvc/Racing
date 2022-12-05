package org.app.controllers;

import org.app.model.GameModel;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Controller {
    public static Map<String, Controller> controllerMap = new HashMap<>();

    public String handle(GameModel gameModels, Logger loggers, String[] args) {
        return null;
    }

    public static void registerController(String controllerName, Controller controller) {
        controllerMap.put(controllerName, controller);
    }

    public static Controller getController(String controllerName) {
        if (controllerMap.containsKey(controllerName)) {
            return controllerMap.get(controllerName);
        }
        return null;
    }
}
