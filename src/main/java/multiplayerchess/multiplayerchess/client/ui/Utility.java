package multiplayerchess.multiplayerchess.client.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.controller.pieces.Piece;

import java.io.IOException;
import java.util.*;

public class Utility {

    // The map of all the images
    private final static Map<String, Image> pieceImages = new HashMap<>();
    private static final List<AutoCloseable> closeables = new ArrayList<>();
    public static String css;

    /**
     * Gets the image for a given piece.
     *
     * @param piece The piece to get the image for.
     * @return The image for the piece.
     */
    public static Image getImage(Piece piece) {
        var image = pieceImages.get(piece.getIconFilename());
        if (image == null) {
            var resource = Utility.class.getResourceAsStream(piece.getIconFilename());
            image = new Image(resource); // TODO: what if it is null?
            pieceImages.put(piece.getIconFilename(), image);
        }
        return image;
    }

    /**
     * Removes a resource from the list of resources to close.
     *
     * @param closeable The resource to remove.
     */
    public static void removeCloseable(AutoCloseable closeable) {
        closeables.remove(closeable);
    }

    /**
     * Adds a resource to the list of resources to close.
     *
     * @param closeable The resource to close eventually.
     */
    public static void addCloseable(AutoCloseable closeable) {
        closeables.add(closeable);
    }

    /**
     * Closes all the registered resources.
     */
    public static void closeAll() {
        for (var closeable : closeables) {
            try {
                closeable.close();
            }
            catch (Exception ignored) {
            }
        }

        closeables.clear();
    }

    /**
     * Loads a new scene and sets the stage to the new scene.
     *
     * @param e        The event that triggered the load.
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
     *
     * @param stage    The stage to set the new scene to.
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
