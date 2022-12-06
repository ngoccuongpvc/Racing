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
        Controller.registerController(EndGameController.__NAME__, new EndGameController());
        Controller.registerController(AnswerController.__NAME__, new AnswerController());
    }

    public void startGame() throws Exception {
        int n_round = 2;
        int duration = 30;

        for (User user : this.gameModel.getReadyUsers()) {
            Controller controller = Controller.getController(StartGameController.__NAME__);
            String response = controller.handle(this.gameModel, this.logger, user, new String[] {});
            user.write(response);
        }

        // Sleep for 3 seconds
        Thread.sleep(3000);

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
                    this.logger.info(String.format("%s: %s", user.username, msg));

                    String[] args = msg.split(" ");

                    if (args.length != 2 || user.isEliminated || args[0].compareTo("ANSWER") != 0) {
                        user.write("INVALID\0");
                        continue;
                    } else {
                        user.write("VALID\0");
                    }
                    Integer answer = Integer.parseInt(args[1]);

                    if (answer.equals(this.gameModel.gameBoard.expectedResult)) {
                        user.point++;
                        user.consecutiveFailedAnswer = 0;
                    } else {
                        user.point--;
                        user.consecutiveFailedAnswer ++;
                        if (user.consecutiveFailedAnswer == 3) {
                            user.isEliminated = true;
                            user.write("ELIMINATED\0");
                        }
                    }
                }
            }

            Controller rankingController = Controller.getController(RankingController.__NAME__);

            for (User user : this.gameModel.getReadyUsers()) {
                String rankingMsg = rankingController.handle(gameModel, logger, user, new String[]{});
                user.write(rankingMsg);
                this.logger.info(rankingMsg);
            }

        }

        for (User user : this.gameModel.getReadyUsers()) {
            Controller controller = Controller.getController(EndGameController.__NAME__);
            String response = controller.handle(this.gameModel, this.logger, user, new String[] {});
            user.write(response);
        }

        this.gameModel.reset();
    }

    @Override
    public void run() {
        this.registerController();

        while (true) {


            while (this.gameModel.numReadyUsers() < 1) {
                Boolean newUserJoined = false;

                for (User user : this.gameModel.getUsers()) {

                    if (user.isReady) {
                        continue;
                    }

                    String msg = null;
                    msg = user.read();
                    if (msg != null) {
                        this.logger.info(msg);
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
                    user.write(response);
                    newUserJoined = true;
                }

                if (newUserJoined) {
                    Controller controller = Controller.getController(InfoController.__NAME__);
                    for (User user : this.gameModel.getReadyUsers()) {
                        String msg = null;
                        try {
                            msg = controller.handle(this.gameModel, this.logger, user, new String[]{});
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        user.write(msg);
                    }
                }
            }

            try {
                this.logger.info("Game start!");
                this.startGame();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
