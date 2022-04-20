package multiplayerchess.multiplayerchess.client.ui;

import multiplayerchess.multiplayerchess.client.controller.Match;

public class ChessGameController {

    private Match match;

    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/ChessGame.fxml";
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
