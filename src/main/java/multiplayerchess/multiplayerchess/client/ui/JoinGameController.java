package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.messages.Message;
import multiplayerchess.multiplayerchess.common.networking.Networking;
import multiplayerchess.multiplayerchess.common.messages.JoinMatchReplyMessage;
import multiplayerchess.multiplayerchess.common.messages.MessageType;

import java.io.IOException;

/**
 * Controller for the join game screen.
 */
public class JoinGameController {

    @FXML
    private TextField MatchIDTextField;

    @FXML
    private Label errorLabel;

    /**
     * Get the file path of the FXML file for this controller's scene.
     * @return The file path of the FXML file for this controller's scene.
     */
    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/JoinGame.fxml";
    }

    public void setupController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handler for the join game button.
     * Attempts to join the game with the given match ID.
     * @param e The click event.
     */
    public void onJoinGame(ActionEvent e) {
        String matchID = MatchIDTextField.getText();

        try {
            networkController = NetworkController.connect(Networking.SERVER_ADDRESS, Networking.SERVER_PORT);

            networkController.addCallback(MessageType.JOIN_GAME, this::joinGameReplyHandler);
            networkController.requestJoinMatch(matchID);
            networkController.start();
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Handler for the back button.
     * Returns the user to the main menu.
     * @param e The click event.
     */
    public void onBack(ActionEvent e) {
        try {
            FXMLLoader loader = Utility.loadNewScene(e, MainMenuController.getFXMLFile());
            MainMenuController controller = loader.getController();
            controller.setupController(stage);
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    private void joinGameReplyHandler(Message message) {
        var reply = (JoinMatchReplyMessage) message;
        networkController.clearCallbacks(MessageType.JOIN_GAME);

        if (reply.success) {
            Platform.runLater(() -> {
                Match startedMatch = new Match(reply.gameStateFEN, reply.player, reply.matchID);

                FXMLLoader loader;
                try {
                    loader = Utility.loadNewScene(stage, ChessGameController.getFXMLFile());
                }
                catch (IOException ex) {
                    //TODO: Handle this - send message to server to dump match
                    return;
                }

                ChessGameController controller = loader.getController();
                controller.setupController(startedMatch, networkController, stage);
            });
        }
        else {
            Platform.runLater(() -> {
                errorLabel.setText("Game Not Found");
                try {
                    networkController.close();
                } catch (IOException ex) {
                    ex.printStackTrace(); //TODO: Check that socket is really closed
                }

            });
        }
    }

    private NetworkController networkController;
    private Stage stage;
}
