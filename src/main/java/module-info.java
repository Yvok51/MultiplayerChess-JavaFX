/**
 * The MultiplayerChess module
 */
module multiplayerchess.multiplayerchess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens multiplayerchess.multiplayerchess to javafx.fxml;
    opens multiplayerchess.multiplayerchess.client to javafx.fxml;
    opens multiplayerchess.multiplayerchess.client.ui to javafx.fxml;

    // Exports everything since otherwise JavaDoc will not generate anything for it
    exports multiplayerchess.multiplayerchess.client;
    exports multiplayerchess.multiplayerchess.client.ui;
    exports multiplayerchess.multiplayerchess.client.networking;
    exports multiplayerchess.multiplayerchess.client.chess;
    exports multiplayerchess.multiplayerchess.client.chess.parsing;
    exports multiplayerchess.multiplayerchess.client.chess.pieces;
    exports multiplayerchess.multiplayerchess.common;
    exports multiplayerchess.multiplayerchess.common.messages;
    exports multiplayerchess.multiplayerchess.common.networking;
    exports multiplayerchess.multiplayerchess.server;
    exports multiplayerchess.multiplayerchess.server.chess;
    exports multiplayerchess.multiplayerchess.server.chess.rules;
    exports multiplayerchess.multiplayerchess.server.chess.pieces;
    exports multiplayerchess.multiplayerchess.server.chess.parsing;
    exports multiplayerchess.multiplayerchess.server.networking;
}