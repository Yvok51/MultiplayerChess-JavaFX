package multiplayerchess.multiplayerchess.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.ui.MainMenuController;
import multiplayerchess.multiplayerchess.client.ui.Utility;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        stage.setTitle("Multiplayer Chess");
        stage.setHeight(800);
        stage.setWidth(800);

        //Utility.loadNewScene(stage, MainMenuController.getFXMLFile());
        stage.setScene(Utility.oldBuild());
    }

    public static void main(String[] args) {
        launch();
    }
}
