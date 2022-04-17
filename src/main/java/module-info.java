module multiplayerchess.multiplayerchess {
    requires javafx.controls;
    requires javafx.fxml;


    opens multiplayerchess.multiplayerchess to javafx.fxml;
    opens multiplayerchess.multiplayerchess.client to javafx.fxml;
    opens multiplayerchess.multiplayerchess.client.ui to javafx.fxml;

    exports multiplayerchess.multiplayerchess;
    exports multiplayerchess.multiplayerchess.client;
}