package multiplayerchess.multiplayerchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.Main;

import java.io.IOException;
import java.util.Objects;

public class Utility {

    public static FXMLLoader loadNewScene(ActionEvent e, String FXMLFile) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        return loadNewScene(stage, FXMLFile);
    }

    public static FXMLLoader loadNewScene(Stage stage, String FXMLFile) throws IOException {
        var url = Utility.class.getResource(FXMLFile);
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(url));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        return null;
    }

    public static Scene oldBuild() throws IOException {
        var url = Main.class.getResource("/multiplayerchess/multiplayerchess/MainMenu.fxml");
        return new Scene(FXMLLoader.load(Objects.requireNonNull(url)));
    }
}
