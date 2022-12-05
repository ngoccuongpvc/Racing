package org.app.controllers;

import org.app.model.GameModel;
import org.app.socket.ConnectionHandler;
import org.app.socket.ConnectionManager;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class GameController extends Thread {

    private Logger logger = null;

    private ConnectionManager connectionManager = null;
    public GameController(ConnectionManager connectionManager, Logger logger) {
        this.connectionManager = connectionManager;
        this.logger = logger;
    }

    private void registerController() {
        Controller.registerController(PingController.__NAME__, new PingController());
        Controller.registerController(JoinRoomController.__NAME__, new JoinRoomController());
        Controller.registerController(InfoController.__NAME__, new InfoController());
    }

    private void sendServerStats(GameModel gameModel, Logger logger, ConnectionHandler connectionHandler) throws Exception {
        Controller infoController = Controller.getController(InfoController.__NAME__);
        String infoResponse = infoController.handle(gameModel, logger, new String[]{});
        connectionHandler.write(infoResponse);
    }
    @Override
    public void run() {
        this.registerController();

        GameModel gameModel = new GameModel();
        while (true) {
            for (Socket connection : this.connectionManager.getConnections()) {
                ConnectionHandler connectionHandler = new ConnectionHandler(connection, logger);

                try {
                    this.sendServerStats(gameModel, this.logger, connectionHandler);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                String msg = connectionHandler.read();
                String[] args = msg.split(" ");
                this.logger.info(args[0]);
                Controller controller = Controller.getController(args[0]);

                if (controller == null) {
                    continue;
                }
                String response = null;
                try {
                    response = controller.handle(gameModel, logger, Arrays.copyOfRange(args, 1, args.length));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                connectionHandler.write(response);
            }
        }
    }
}
