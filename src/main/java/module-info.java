/**
 * Contains both the server and client applications.
 */
module multiplayerchess.multiplayerchess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens multiplayerchess.multiplayerchess to javafx.fxml;
    opens multiplayerchess.multiplayerchess.client to javafx.fxml;
    opens multiplayerchess.multiplayerchess.client.ui to javafx.fxml;

    exports multiplayerchess.multiplayerchess.client;
    exports multiplayerchess.multiplayerchess.server;
}