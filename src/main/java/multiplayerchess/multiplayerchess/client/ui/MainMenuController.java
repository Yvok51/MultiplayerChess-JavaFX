package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import multiplayerchess.multiplayerchess.client.networking.INetworkController;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.Networking;

import java.io.IOException;

/**
 * Controller for the main menu scene.
 */
public class MainMenuController {

    /**
     * Get the file path of the FXML file for this controler's scene.
     * @return The file path of the FXML file for this controler's scene.
     */
    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/MainMenu.fxml";
    }

    /**
     * Handler for the "Start Game" button.
     * @param e The click event.
     */
    public void onStartGame(ActionEvent e) {
        try {
            INetworkController networkController = NetworkController.connect(Networking.SERVER_ADDR, Networking.SERVER_PORT);

            var startedMatch = networkController.startMatch();
            if (startedMatch.isEmpty()) {
                throw new IOException("Could not start match - could not connect to server.");
            }

            FXMLLoader loader = Utility.loadNewScene(e, ChessGameController.getFXMLFile());

            ChessGameController controller = loader.getController();
            controller.setupController(startedMatch.get(), networkController, Utility.getStageFromEvent(e));

        } catch (IOException ex) {
           System.err.println(ex.getMessage());
            // Platform.exit();
        }
    }

    /**
     * Handler for the "Join Game" button.
     * @param e The click event.
     */
    public void onJoinGame(ActionEvent e) {
        try {
            Utility.loadNewScene(e, JoinGameController.getFXMLFile());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            Platform.exit();
        }
    }

    /**
     * Handler for the "Exit" button.
     * @param e The click event.
     */
    public void onQuit(ActionEvent e) {
        Platform.exit();
    }
}
