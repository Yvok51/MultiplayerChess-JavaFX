package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.Main;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.Networking;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    private static Stage stage;

    public static void setStage(Stage stage) {
        MainMenuController.stage = stage;
    }

    public static Scene buildStage() throws IOException {
        var url = Main.class.getResource("/multiplayerchess/multiplayerchess/MainMenu.fxml");
        return new Scene(FXMLLoader.load(Objects.requireNonNull(url)));
    }

    public void onStartGame(ActionEvent e) {
        try {
            var networkController = NetworkController.connect(Networking.SERVER_ADDR, Networking.SERVER_PORT);
            var success = networkController.sendStartMatch();
            if (!success) {
            }
            var reply = networkController.receiveReply();
            if (reply.isEmpty()) {

            }


        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    public void onJoinGame(ActionEvent e) {
        try {
            stage.setScene(JoinGameController.buildStage());
            JoinGameController.setStage(stage);
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }

    public void onQuit(ActionEvent e) {
        Platform.exit();
    }
}
