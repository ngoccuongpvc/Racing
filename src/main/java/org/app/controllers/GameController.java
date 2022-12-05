package org.app.controllers;

import org.app.model.GameModel;
import org.app.socket.ConnectionHandler;
import org.app.socket.ConnectionManager;

import java.lang.reflect.Array;
import java.net.Socket;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public class GameController extends Thread {

    private Logger logger = null;

    private ConnectionManager connectionManager = null;

    private GameModel gameModel = null;
    public GameController(ConnectionManager connectionManager, Logger logger) {
        this.connectionManager = connectionManager;
        this.logger = logger;
        this.gameModel = new GameModel();
    }

    private void registerController() {
        Controller.registerController(PingController.__NAME__, new PingController());
        Controller.registerController(JoinRoomController.__NAME__, new JoinRoomController());
        Controller.registerController(InfoController.__NAME__, new InfoController());
        Controller.registerController(RankingController.__NAME__, new RankingController());
    }

    private void sendServerStats(GameModel gameModel, Logger logger, ConnectionHandler connectionHandler) throws Exception {
        Controller infoController = Controller.getController(InfoController.__NAME__);
        String infoResponse = infoController.handle(gameModel, logger, new String[]{});
        connectionHandler.write(infoResponse);
    }


    public void startGame() throws Exception {
        int n_round = 2;
        int duration = 30;

        Random rand = new Random();

        for (Socket connection : this.connectionManager.getConnections()) {
            ConnectionHandler connectionHandler = new ConnectionHandler(connection, this.logger);
            connectionHandler.write("START_GAME\0");
        }

        for (int round = 1; round <= n_round; ++round) {
            int a = rand.nextInt(100);
            int b = rand.nextInt(100);

            Instant timestamp = Instant.now();

            for (Socket connection : this.connectionManager.getConnections()) {
                ConnectionHandler connectionHandler = new ConnectionHandler(connection, this.logger);
                connectionHandler.write("QUESTION " + Integer.toString(a) + " + "+ Integer.toString(b) + " " + Long.toString(timestamp.getEpochSecond()) + " 30\0");
            }

            Boolean isFirst = true;

            while (timestamp.getEpochSecond() + duration > Instant.now().getEpochSecond()) {
                for (Socket connection : this.connectionManager.getConnections()) {
                    ConnectionHandler connectionHandler = new ConnectionHandler(connection, this.logger);

                    String msg = connectionHandler.read();

                    String[] args = msg.split(" ");

                    if (args.length != 2) {
                        connectionHandler.write("INVALID\0");
                        continue;
                    } else {
                        connectionHandler.write("VALID\0");
                    }
                    Integer answer = Integer.parseInt(args[1]);

                    if (answer == a + b) {

                    }
                }
                break;
            }

            Controller rankingController = Controller.getController(RankingController.__NAME__);
            String rankingMsg = rankingController.handle(gameModel, logger, new String[]{});
            for (Socket connection : this.connectionManager.getConnections()) {
                ConnectionHandler connectionHandler = new ConnectionHandler(connection, this.logger);

                connectionHandler.write(rankingMsg);
            }

        }
    }

    @Override
    public void run() {
        this.registerController();

        while (true) {
            for (Socket connection : this.connectionManager.getConnections()) {
                ConnectionHandler connectionHandler = new ConnectionHandler(connection, logger);

                try {
                    this.sendServerStats(this.gameModel, this.logger, connectionHandler);
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
                    response = controller.handle(this.gameModel, logger, Arrays.copyOfRange(args, 1, args.length));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                connectionHandler.write(response);
            }

            if (this.gameModel.getUsernames().size() > 0) {
                try {
                    this.startGame();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
