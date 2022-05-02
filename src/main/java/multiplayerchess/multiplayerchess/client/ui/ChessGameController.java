package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.client.controller.Move;
import multiplayerchess.multiplayerchess.client.controller.Winner;
import multiplayerchess.multiplayerchess.client.networking.INetworkController;
import multiplayerchess.multiplayerchess.client.networking.TurnReplyStatus;

import java.io.IOException;

public class ChessGameController {

    @FXML
    private AnchorPane chessGamePane;

    @FXML
    private Button resignButton;

    @FXML
    private Label matchIDLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Text errorText;

    private Match match;
    private INetworkController networkController;

    private Stage stage;
    private UIBoard board;

    private UIPopup popup;

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
    public void setupController(Match match, INetworkController networkController, Stage stage) {
        this.stage = stage;
        this.networkController = networkController;
        this.match = match;
        board = new UIBoard(match.getPlayer(), this);
        board.setupBoard(match.getBoard());

        chessGamePane.getChildren().add(board);

        AnchorPane.setTopAnchor(board, 40.0);
        AnchorPane.setLeftAnchor(board, 160.0);

        popup = new UIPopup(stage, "OK");
        popup.setButtonAction(event -> {
            popup.hide();
            // Go back to main menu
            try {
                Utility.loadNewScene(stage, MainMenuController.getFXMLFile());
            } catch (IOException ex) {
                ex.printStackTrace();
                Platform.exit();
            }
        });

        this.newTurn();
        matchIDLabel.setText("Match ID: " + match.getMatchID());

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
            match.nextTurn(message.gameStateFEN);
            board.setupBoard(match.getBoard());
            this.newTurn();

            if (message.gameOver) {
                endMatch(Winner.getWinnerFromPlayer(message.winner), "Game over");
            }
        }
        else if (message.status == TurnReplyStatus.TURN_REJECTED) {
            // TODO: Show error message
            return;
        }
    }

    public void onResignAndQuit() {
        networkController.sendResign(match.getMatchID());
        endMatch(Winner.getWinnerFromPlayer(match.getPlayer().opposite()), "Resignation");
    }

    /**
     * Ends the match and displays a message to the user.
     * @param winner The winner of the match.
     * @param reason The reason the match ended.
     */
    private void endMatch(Winner winner, String reason) {
        try {
            networkController.close();
        } catch (IOException ignored) {
        }

        String winText = getWinnerText(winner) + ": " + reason;
        popup.show(winText);

        this.disable();

    }

    private String getWinnerText(Winner winner) {
        String winText = winner.toString();
        if (winner != Winner.NONE) {
            winText += " Won";
        }
        return winText;
    }

    private void disable() {
        board.disable();
        resignButton.setDisable(true);
    }

    private void newTurn() {
        errorText.setText("");
        setTurnLabel();
    }

    private void setTurnLabel() {
        if (match.isOurTurn()) {
            turnLabel.setText("Your turn");
        }
        else {
            turnLabel.setText("Opponent's turn");
        }
    }
}