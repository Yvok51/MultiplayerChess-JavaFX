package multiplayerchess.multiplayerchess.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var url = Main.class.getResource("/multiplayerchess/multiplayerchess/MainMenu.fxml");
        //Parent root = FXMLLoader.load(Objects.requireNonNull(url));
        /*/
        if (url == null) {
            System.out.println("Could not load resource: MainMenu.fxml");
        }
        Group root = new Group();
        /**/
        stage.setResizable(false);
        stage.setTitle("Multiplayer Chess");
        stage.setHeight(800);
        stage.setWidth(800);

        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(url)));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
