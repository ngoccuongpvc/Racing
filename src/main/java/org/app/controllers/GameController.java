package org.app.controllers;

import org.app.model.GameModel;
import org.app.model.User;
import org.app.socket.ConnectionHandler;
import org.app.socket.ConnectionManager;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public class GameController extends Thread {

    private Logger logger = null;

    private GameModel gameModel = null;
    public GameController(Logger logger, GameModel gameModel) {
        this.logger = logger;
        this.gameModel = gameModel;
    }

    private void registerController() {
        Controller.registerController(PingController.__NAME__, new PingController());
        Controller.registerController(JoinRoomController.__NAME__, new JoinRoomController());
        Controller.registerController(InfoController.__NAME__, new InfoController());
        Controller.registerController(RankingController.__NAME__, new RankingController());
        Controller.registerController(StartGameController.__NAME__, new StartGameController());
        Controller.registerController(QuestionController.__NAME__, new QuestionController());
    }

    public void startGame() throws Exception {
        int n_round = 2;
        int duration = 30;

        Random rand = new Random();

        for (User user : this.gameModel.getReadyUsers()) {
            Controller controller = Controller.getController(StartGameController.__NAME__);
            String response = controller.handle(this.gameModel, this.logger, user, new String[] {});
            user.write(response);
        }

        for (int round = 1; round <= n_round; ++round) {
            this.gameModel.startNewRound();

            for (User user : this.gameModel.getReadyUsers()) {
                String questionMessage = Controller.getController(QuestionController.__NAME__).handle(this.gameModel, this.logger, user, new String[]{});
                user.write(questionMessage);
            }



            Boolean isFirst = true;

            while (this.gameModel.gameBoard.timestamp.getEpochSecond() + duration > Instant.now().getEpochSecond()) {
                for (User user : this.gameModel.getReadyUsers()) {

                    if (user.answer != null) {
                        continue;
                    }

                    String msg = user.read();

                    if (msg == null) {
                        continue;
                    }

                    String[] args = msg.split(" ");

                    if (args.length != 2 || args[0].compareTo("ANSWER") != 0) {
                        user.write("INVALID\0");
                        continue;
                    } else {
                        user.write("VALID\0");
                    }
                    Integer answer = Integer.parseInt(args[1]);

                    if (answer.equals(this.gameModel.gameBoard.expectedResult)) {
                        user.point++;
                    }
                }
                break;
            }

            Controller rankingController = Controller.getController(RankingController.__NAME__);

            for (User user : this.gameModel.getReadyUsers()) {
                String rankingMsg = rankingController.handle(gameModel, logger, user, new String[]{});
                user.write(rankingMsg);
            }

        }
    }

    @Override
    public void run() {
        this.registerController();


        while (this.gameModel.numReadyUsers() < 1) {
            for (User user : this.gameModel.getUsers()) {

                if (user.isReady) {
                    continue;
                }

                String msg = null;
                try {
                    msg = user.read();
                    if (msg != null) {
                        this.logger.info(msg);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (msg == null) {
                    continue;
                }

                String[] args = msg.split(" ");
                Controller controller = Controller.getController(args[0]);

                if (controller == null) {
                    continue;
                }

                String response = null;
                try {
                    response = controller.handle(this.gameModel, logger, user, Arrays.copyOfRange(args, 1, args.length));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    user.write(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            this.startGame();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
