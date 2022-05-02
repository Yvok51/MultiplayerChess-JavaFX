package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.Networking;

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
     * Get the file path of the FXML file for this controler's scene.
     * @return The file path of the FXML file for this controler's scene.
     */
    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/JoinGame.fxml";
    }

    /**
     * Handler for the join game button.
     * Attempts to join the game with the given match ID.
     * @param e The click event.
     */
    public void onJoinGame(ActionEvent e) {
        String matchID = MatchIDTextField.getText();

        try {
            var networkController = NetworkController.connect(Networking.SERVER_ADDR, Networking.SERVER_PORT);
            var match = networkController.joinMatch(matchID);
            if (match.isEmpty()) {
                errorLabel.setText("Game Not Found");
                return;
            }

            FXMLLoader loader = Utility.loadNewScene(e, ChessGameController.getFXMLFile());

            ChessGameController controller = loader.getController();
            controller.setupController(match.get(), networkController, Utility.getStageFromEvent(e));

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
            Utility.loadNewScene(e, MainMenuController.getFXMLFile());
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }
}
