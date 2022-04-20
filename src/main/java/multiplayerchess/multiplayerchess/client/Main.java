package multiplayerchess.multiplayerchess.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.ui.MainMenuController;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        stage.setTitle("Multiplayer Chess");
        stage.setHeight(800);
        stage.setWidth(800);

        MainMenuController.setStage(stage);
        stage.setScene(MainMenuController.buildStage());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
