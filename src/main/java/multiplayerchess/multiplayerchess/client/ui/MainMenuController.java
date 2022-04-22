package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.Main;
import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.client.networking.INetworkController;
import multiplayerchess.multiplayerchess.client.networking.MockNetworkController;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.Networking;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/MainMenu.fxml";
    }

    public void onStartGame(ActionEvent e) {
        try {
            INetworkController networkController = new MockNetworkController(); // NetworkController.connect(Networking.SERVER_ADDR, Networking.SERVER_PORT);

            var startedMatch = networkController.StartMatch();
            if (startedMatch.isEmpty()) {
                throw new IOException("Could not start match");
            }

            FXMLLoader loader = Utility.loadNewScene(e, ChessGameController.getFXMLFile());

            ChessGameController controller = loader.getController();
            controller.setMatch(startedMatch.get());

        } catch (IOException ex) {
            ex.printStackTrace();
            // Platform.exit();
        }
    }

    public void onJoinGame(ActionEvent e) {
        try {
            Utility.loadNewScene(e, JoinGameController.getFXMLFile());
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    public void onQuit(ActionEvent e) {
        Platform.exit();
    }
}
