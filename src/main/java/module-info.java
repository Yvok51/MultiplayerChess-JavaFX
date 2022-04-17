module multiplayerchess.multiplayerchess {
    requires javafx.controls;
    requires javafx.fxml;


    opens multiplayerchess.multiplayerchess to javafx.fxml;
    exports multiplayerchess.multiplayerchess;
}