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
    // The map of all the images
    public static Map<String, Image> pieceImages = new HashMap<>();

    /**
     * Gets the image for a given piece.
     * @param piece The piece to get the image for.
     * @return The image for the piece.
     */
    public static Image getImage(Piece piece) {
        var image = pieceImages.get(piece.getIconFilename());
        if (image == null) {
            var resource = Utility.class.getResourceAsStream(piece.getIconFilename());
            image = new Image(resource);
            pieceImages.put(piece.getIconFilename(), image);
        }
        return image;
    }

    /**
     * Gets the stage from an event.
     * @param event The event to get the stage from.
     * @return The stage got from the event.
     */
    public static Stage getStageFromEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    /**
     * Loads a new scene and sets the stage to the new scene.
     * @param e The event that triggered the load.
     * @param FXMLFile The FXML file to load.
     * @return The FXMLLoader for the new scene.
     * @throws IOException If the FXML file could not be loaded.
     */
    public static FXMLLoader loadNewScene(ActionEvent e, String FXMLFile) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        return loadNewScene(stage, FXMLFile);
    }

    /**
     * Loads a new scene and sets the stage to the new scene.
     * @param stage The stage to set the new scene to.
     * @param FXMLFile The FXML file to load.
     * @return The FXMLLoader for the new scene.
     * @throws IOException If the FXML file could not be loaded.
     */
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
