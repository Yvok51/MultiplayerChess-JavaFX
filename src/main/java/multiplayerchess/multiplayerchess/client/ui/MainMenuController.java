package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.chess.Match;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.messages.Message;
import multiplayerchess.multiplayerchess.common.messages.MessageType;
import multiplayerchess.multiplayerchess.common.messages.StartGameReplyMessage;
import multiplayerchess.multiplayerchess.common.networking.Networking;

import java.io.IOException;

/**
 * Controller for the main menu scene.
 */
public class MainMenuController {

    private boolean opponentConnected = false;
    private Stage stage;
    private NetworkController networkController;

    /**
     * Get the file path of the FXML file for this controller's scene.
     *
     * @return The file path of the FXML file for this controller's scene.
     */
    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/MainMenu.fxml";
    }

    /**
     * Sets up the controller after its construction, since the constructor is called by the JavFX framework.
     *
     * @param stage The stage the scene is displayed on.
     */
    public void setupController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handler for the "Start Game" button.
     *
     * @param e The click event.
     */
    public void onStartGame(ActionEvent e) {
        try {
            networkController = NetworkController.connect(Networking.SERVER_ADDRESS, Networking.SERVER_PORT);

            networkController.addCallback(MessageType.START_GAME, this::startGameReplyHandler);
            networkController.addCallback(MessageType.CONNECTED, this::opponentConnectedHandler);
            networkController.requestNewMatch();
            networkController.start();
            Utility.addCloseable(networkController);
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
            Platform.exit();
        }
    }

    /**
     * Handler for the "Join Game" button.
     *
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
     *
     * @param e The click event.
     */
    public void onQuit(ActionEvent e) {
        Platform.exit();
    }

    /**
     * Handles to the {@link StartGameReplyMessage}. Starts the match and switches to the game scene.
     *
     * @param message The message to handle.
     */
    private void startGameReplyHandler(Message message) {
        var reply = (StartGameReplyMessage) message;
        networkController.clearAllCallbacks();

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
                controller.setupController(startedMatch, networkController, stage, opponentConnected);
            });
        } else {
            //TODO: Signify to the user that the match could not be started
        }
    }

    /**
     * Handler for the {@link multiplayerchess.multiplayerchess.common.messages.OpponentConnectedMessage} message.
     * Used for the case we did not have the time to setup the next controller before receiving the message.
     *
     * @param message The message to handle.
     */
    private void opponentConnectedHandler(Message message) {
        opponentConnected = true;
    }
}
