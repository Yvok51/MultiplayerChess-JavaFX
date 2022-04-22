package multiplayerchess.multiplayerchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.controller.pieces.Piece;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utility {

    public static String css;
    public static Map<String, ImageView> pieceImages = new HashMap<>();

    public static ImageView getImageView(Piece piece) {
        var image = pieceImages.get(piece.getIconFilename());
        if (image == null) {
            var resource = Utility.class.getResourceAsStream(piece.getIconFilename());
            image = new ImageView(new Image(resource));
            pieceImages.put(piece.getIconFilename(), image);
        }
        return image;
    }

    public static FXMLLoader loadNewScene(ActionEvent e, String FXMLFile) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        return loadNewScene(stage, FXMLFile);
    }

    public static FXMLLoader loadNewScene(Stage stage, String FXMLFile) throws IOException {
        var url = Utility.class.getResource(FXMLFile);
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(url));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        stage.setScene(scene);

        return loader;
    }

}
