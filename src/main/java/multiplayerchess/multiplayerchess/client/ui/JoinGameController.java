package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.Main;

import java.io.IOException;
import java.util.Objects;

public class JoinGameController {
    private static Stage stage;

    public static void setStage(Stage stage) {
        JoinGameController.stage = stage;
    }

    public static Scene buildStage() throws IOException {
        var url = Main.class.getResource("/multiplayerchess/multiplayerchess/JoinGame.fxml");
        return new Scene(FXMLLoader.load(Objects.requireNonNull(url)));
    }

    public void onJoinGame() {

    }

    public void onBack() {
        try {
            stage.setScene(MainMenuController.buildStage());
            MainMenuController.setStage(stage);
        } catch (IOException ex) {
            ex.printStackTrace();
            Platform.exit();
        }
    }
}
