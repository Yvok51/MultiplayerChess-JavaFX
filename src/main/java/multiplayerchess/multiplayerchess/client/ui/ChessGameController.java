package multiplayerchess.multiplayerchess.client.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.client.controller.Move;
import multiplayerchess.multiplayerchess.client.controller.Winner;
import multiplayerchess.multiplayerchess.client.networking.INetworkController;
import multiplayerchess.multiplayerchess.client.networking.TurnReplyStatus;
import multiplayerchess.multiplayerchess.common.messages.OpponentDisconnectedMessage;
import multiplayerchess.multiplayerchess.common.messages.OpponentResignedMessage;
import multiplayerchess.multiplayerchess.common.messages.TurnReplyMessage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChessGameController {

    @FXML
    private AnchorPane chessGamePane;

    private Match match;
    private UIBoard board;

    private INetworkController networkController;

    /**
     * Get the file path of the FXML file for this controler's scene.
     * @return The file path of the FXML file for this controler's scene.
     */
    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/ChessGame.fxml";
    }

    /**
     * Setup this controller
     * Since the JavaFX framework calls the constructor we need to setup the board here.
     * @param match The match the controller handles.
     * @param networkController The network controller to use.
     */
    public void setupController(Match match, INetworkController networkController) {
        this.networkController = networkController;
        this.match = match;
        board = new UIBoard(match.getPlayer(), this);
        board.setupBoard(match.getBoard());

        chessGamePane.getChildren().add(board);

        AnchorPane.setTopAnchor(board, 60.0);
        AnchorPane.setLeftAnchor(board, 140.0);
    }

    /**
     * Called when the user clicks on a square in order to move a selected piece.
     * Calls the server to perform the move and if everything is successful, updates the board,
     * otherwise displays an error message.
     * @param move The move to make.
     */
    public void movePiece(Move move) {
        var reply = networkController.sendTurn(move.getPieceType(), move.getStartPosition(),
                move.getEndPosition(), match.getPlayer().getColor(), move.isCapture(), match.getMatchID());

        if (reply.isEmpty()) {
            return;
        }

        var message = reply.get();
        if (message.status == TurnReplyStatus.OPPONENT_RESIGNED) {
            endMatch(Winner.getWinnerFromPlayer(match.getPlayer()), "Opponent resigned");
        }
        else if (message.status == TurnReplyStatus.OPPONENT_DISCONNECTED) {
            endMatch(Winner.getWinnerFromPlayer(match.getPlayer()), "Opponent disconnected");
        }
        else if (message.status == TurnReplyStatus.TURN_ACCEPTED) {
            match.updateBoard(message.gameStateFEN);
            board.setupBoard(match.getBoard());

            if (message.gameOver) {
                endMatch(Winner.getWinnerFromPlayer(message.winner), "Game over");
            }
        }
        else if (message.status == TurnReplyStatus.TURN_REJECTED) {
            // TODO: Show error message
            return;
        }
    }

    /**
     * Ends the match and displays a message to the user.
     * @param winner The winner of the match.
     * @param reason The reason the match ended.
     */
    private void endMatch(Winner winner, String reason) {
        // TODO: Implement

    }
}