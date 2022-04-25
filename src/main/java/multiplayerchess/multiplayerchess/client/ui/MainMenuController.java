package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import multiplayerchess.multiplayerchess.client.networking.INetworkController;
import multiplayerchess.multiplayerchess.client.networking.MockNetworkController;

import java.io.IOException;

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
            controller.setupController(startedMatch.get(), networkController);

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
