package multiplayerchess.multiplayerchess.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import multiplayerchess.multiplayerchess.client.controller.Match;

import java.net.URL;
import java.util.ResourceBundle;

public class ChessGameController implements Initializable {

    @FXML
    private AnchorPane chessGamePane;

    private Match match;
    private UIBoard board;

    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/ChessGame.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setMatch(Match match) {
        this.match = match;
        board = new UIBoard(match.getPlayer());
        board.setupBoard(match.getBoard());
        chessGamePane.getChildren().add(board);
    }
}