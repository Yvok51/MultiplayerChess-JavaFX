package multiplayerchess.multiplayerchess.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.ui.MainMenuController;
import multiplayerchess.multiplayerchess.client.ui.Utility;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    /**
     * Starts the application.
     *
     * @param stage The stage to start the application on.
     * @throws IOException If the FXML file for the next scene could not be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        stage.setTitle("Multiplayer Chess");
        stage.setHeight(800);
        stage.setWidth(800);

        var url = Main.class.getResource("/multiplayerchess/multiplayerchess/style.css");
        if (url == null) {
            System.err.println("Could not load style.css");
            Platform.exit();
            return;
        }

        Utility.css = url.toExternalForm();

        FXMLLoader loader = Utility.loadNewScene(stage, MainMenuController.getFXMLFile());
        MainMenuController controller = loader.getController();
        controller.setupController(stage);

        stage.show();
    }

    /**
     * Called when the application is about to be closed.
     */
    @Override
    public void stop() {
        // end all threads still running
        Utility.closeAll();
    }
}
