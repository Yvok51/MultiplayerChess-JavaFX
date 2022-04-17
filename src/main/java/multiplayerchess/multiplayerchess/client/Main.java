package multiplayerchess.multiplayerchess.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../stages/MainMenu.fxml")));
        // Group root = new Group();

        stage.setResizable(false);
        stage.setTitle("Multiplayer Chess");
        stage.setHeight(800);
        stage.setWidth(800);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
