package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.messages.Message;
import multiplayerchess.multiplayerchess.common.networking.Networking;
import multiplayerchess.multiplayerchess.common.messages.MessageType;
import multiplayerchess.multiplayerchess.common.messages.StartGameReplyMessage;

import java.io.IOException;

/**
 * Controller for the main menu scene.
 */
public class MainMenuController {

    /**
     * Get the file path of the FXML file for this controller's scene.
     * @return The file path of the FXML file for this controller's scene.
     */
    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/MainMenu.fxml";
    }

    public void setupController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handler for the "Start Game" button.
     * @param e The click event.
     */
    public void onStartGame(ActionEvent e) {
        try {
            networkController = NetworkController.connect(Networking.SERVER_ADDRESS, Networking.SERVER_PORT);

            networkController.addCallback(MessageType.START_GAME, this::startGameReplyHandler);
            networkController.requestNewMatch();
            networkController.start();
        }
        catch (IOException ex) {
           System.err.println(ex.getMessage());
            Platform.exit();
        }
    }

    /**
     * Handler for the "Join Game" button.
     * @param e The click event.
     */
    public void onJoinGame(ActionEvent e) {
        try {
            FXMLLoader loader = Utility.loadNewScene(e, JoinGameController.getFXMLFile());
            JoinGameController controller = loader.getController();
            controller.setupController(stage);
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
            Platform.exit();
        }
    }

    /**
     * Handler for the "Quit" button.
     * @param e The click event.
     */
    public void onQuit(ActionEvent e) {
        Platform.exit();
    }

    private void startGameReplyHandler(Message message) {
        var reply = (StartGameReplyMessage) message;
        networkController.clearCallbacks(MessageType.START_GAME);

        if (reply.success) {
            Platform.runLater(() -> {
                Match startedMatch = new Match(reply.startingFEN, reply.player, reply.matchID);

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
            //TODO: Signify to the user that the match could not be started
        }
    }

    private Stage stage;
    private NetworkController networkController;
}
