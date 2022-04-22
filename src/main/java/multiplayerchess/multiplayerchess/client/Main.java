package multiplayerchess.multiplayerchess.client;

import javafx.application.Application;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.ui.MainMenuController;
import multiplayerchess.multiplayerchess.client.ui.Utility;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        stage.setTitle("Multiplayer Chess");
        stage.setHeight(800);
        stage.setWidth(800);

        var url = Main.class.getResource("/multiplayerchess/multiplayerchess/style.css");
        Utility.css = url.toExternalForm();

        Utility.loadNewScene(stage, MainMenuController.getFXMLFile());

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
