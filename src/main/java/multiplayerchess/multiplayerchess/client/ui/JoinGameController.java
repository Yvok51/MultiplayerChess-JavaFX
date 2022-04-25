package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.Networking;

import java.io.IOException;

public class JoinGameController {

    @FXML
    private TextField MatchIDTextField;

    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/JoinGame.fxml";
    }

    public void onJoinGame(ActionEvent e) {
        String matchID = MatchIDTextField.getText();

        try {
            var networkController = NetworkController.connect(Networking.SERVER_ADDR, Networking.SERVER_PORT);
            var match = networkController.joinMatch(matchID);
            if (match.isEmpty()) {
                throw new IOException("Match not found");
            }

            FXMLLoader loader = Utility.loadNewScene(e, ChessGameController.getFXMLFile());

            ChessGameController controller = loader.getController();
            controller.setupController(match.get(), networkController);

        } catch (IOException ex) {
            ex.printStackTrace();
            // Platform.exit();
        }
    }

    public void onBack(ActionEvent e) {
        try {
            Utility.loadNewScene(e, MainMenuController.getFXMLFile());
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }
}
