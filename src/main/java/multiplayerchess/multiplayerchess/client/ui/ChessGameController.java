package multiplayerchess.multiplayerchess.client.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import multiplayerchess.multiplayerchess.client.chess.Match;
import multiplayerchess.multiplayerchess.client.chess.Move;
import multiplayerchess.multiplayerchess.client.chess.Winner;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.common.messages.Message;
import multiplayerchess.multiplayerchess.common.messages.MessageType;
import multiplayerchess.multiplayerchess.common.messages.TurnReplyMessage;

import java.io.IOException;

/**
 * The controller for the chess game.
 */
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

    @FXML
    private Pane popupPane;
    @FXML
    private Label popupLabel;
    @FXML
    private Button popupButton;

    private Match match;
    private NetworkController networkController;

    private UIBoard board;

    /**
     * Get the file path of the FXML file for this controller's scene.
     *
     * @return The file path of the FXML file for this controller's scene.
     */
    public static String getFXMLFile() {
        return "/multiplayerchess/multiplayerchess/ChessGame.fxml";
    }

    /**
     * Setup this controller
     * Since the JavaFX framework calls the constructor we need to set up the board here.
     *
     * @param match             The match the controller handles.
     * @param networkController The network controller to use.
     * @param stage             The stage the scene is displayed on.
     * @param gameUnderWay      Whether the match is already under way or not.
     */
    public void setupController(Match match, NetworkController networkController, Stage stage, boolean gameUnderWay) {
        this.networkController = networkController;
        this.match = match;

        networkController.addCallback(MessageType.TURN, this::turnHandler);
        networkController.addCallback(MessageType.RESIGNED, this::opponentResignedHandler);
        networkController.addCallback(MessageType.DISCONNECTED, this::opponentDisconnectedHandler);
        if (!gameUnderWay) {
            networkController.addCallback(MessageType.CONNECTED, this::opponentJoinedHandler);
        }

        board = new UIBoard(match.getPlayer(), this);
        board.setupBoard(match.getBoard());

        chessGamePane.getChildren().add(board);

        AnchorPane.setTopAnchor(board, 40.0);
        AnchorPane.setLeftAnchor(board, 160.0);

        this.setWaitingForOpponent();

        popupPane.setOpacity(1);
        popupLabel.setOpacity(1);
        popupButton.setOpacity(1);

        popupButton.setOnAction(event -> {
            popupPane.setVisible(false);
            // Go back to main menu
            try {
                Utility.removeCloseable(networkController);
                FXMLLoader loader = Utility.loadNewScene(stage, MainMenuController.getFXMLFile());
                MainMenuController controller = loader.getController();
                controller.setupController(stage);
            }
            catch (IOException ex) {
                ex.printStackTrace();
                Platform.exit();
            }
        });

        this.newTurn();
        matchIDLabel.setText("Match ID: " + match.getMatchID());

        // TODO: This is ugly.
        if (gameUnderWay) {
            opponentJoinedHandler(null);
        }
    }

    /**
     * Handler for the {@link multiplayerchess.multiplayerchess.common.messages.OpponentResignedMessage}
     *
     * @param message The message to handle.
     */
    public void opponentResignedHandler(Message message) {
        Platform.runLater(() -> endMatch(Winner.getWinnerFromPlayer(match.getPlayer()), "Opponent resigned"));
    }

    /**
     * Handler for the {@link multiplayerchess.multiplayerchess.common.messages.OpponentDisconnectedMessage}
     *
     * @param message The message to handle.
     */
    public void opponentDisconnectedHandler(Message message) {
        Platform.runLater(() -> endMatch(Winner.getWinnerFromPlayer(match.getPlayer()), "Opponent disconnected"));
    }

    /**
     * Handler for the {@link multiplayerchess.multiplayerchess.common.messages.TurnReplyMessage}
     * Depending on the message, the match is updated.
     *
     * @param message The message to handle.
     */
    public void turnHandler(Message message) {
        TurnReplyMessage reply = (TurnReplyMessage) message;

        if (reply.success) {
            Platform.runLater(() -> {
                match.nextTurn(reply.gameStateFEN);
                board.setupBoard(match.getBoard());
                this.newTurn();

                if (reply.gameOver) {
                    endMatch(Winner.getWinnerFromPlayer(reply.winner), "Game over");
                }
            });
        } else {
            Platform.runLater(() -> errorText.setText("Invalid move"));
        }
    }

    /**
     * Handler for the {@link multiplayerchess.multiplayerchess.common.messages.OpponentConnectedMessage}
     * Starts the game.
     *
     * @param message The message to handle.
     */
    public void opponentJoinedHandler(Message message) {
        popupPane.setVisible(false);
        this.setDisable(false);
    }

    /**
     * Called when the user clicks on a square in order to move a selected piece.
     * Calls the server to perform the move and if everything is successful, updates the board,
     * otherwise displays an error message.
     *
     * @param move The move to make.
     */
    public void movePiece(Move move) {
        networkController.sendTurn(move.getPieceType(), move.getStartPosition(),
                move.getEndPosition(), match.getPlayer().getColor(), move.isCapture(), match.getMatchID());
    }

    /**
     * Called when the user clicks the resign button.
     */
    public void onResignAndQuit() {
        networkController.sendResign(match.getMatchID(), match.getPlayer());
        endMatch(Winner.getWinnerFromPlayer(match.getPlayer().opposite()), "Resignation");
    }

    /**
     * Set the popup pane to visible and disable the game for the case we are still waiting for an opponent.
     */
    private void setWaitingForOpponent() {
        board.setDisable(true);

        popupPane.setVisible(true);
        popupPane.setDisable(false);

        popupLabel.setText("Waiting For Opponent...");
        popupLabel.setVisible(true);

        popupButton.setVisible(false);
        popupButton.setDisable(true);
    }

    /**
     * Answers whether the move is a capturing move.
     *
     * @param type  The type of the piece being moved.
     * @param start The starting position of the piece.
     * @param end   The ending position of the piece.
     * @return True if the move is a capturing move, false otherwise.
     */
    public boolean isCapture(PieceType type, Position start, Position end) {
        return match.isCapture(type, start, end);
    }

    /**
     * Ends the match and displays a message to the user.
     *
     * @param winner The winner of the match.
     * @param reason The reason the match ended.
     */
    private void endMatch(Winner winner, String reason) {
        try {
            networkController.close();
        }
        catch (IOException ignored) {
        }

        String winText = getWinnerText(winner) + ": " + reason;
        popupLabel.setText(winText);
        popupPane.setVisible(true);

        popupButton.setVisible(true);
        popupButton.setDisable(false);

        this.setDisable(true);
    }

    /**
     * Returns the winner text for the given winner.
     *
     * @param winner The winner of the match.
     * @return The winner text for the given winner.
     */
    private String getWinnerText(Winner winner) {
        String winText = winner.toString();
        if (winner != Winner.NONE) {
            winText += " Won";
        }
        return winText;
    }

    /**
     * Sets whether everything is disabled or not.
     *
     * @param disable True if everything is to be disabled, false otherwise.
     */
    private void setDisable(boolean disable) {
        board.setDisable(disable);
        resignButton.setDisable(disable);
    }

    /**
     * Starts a new turn
     */
    private void newTurn() {
        errorText.setText("");
        setTurnLabel();
    }

    /**
     * Sets the turn label to the player whose turn it is.
     */
    private void setTurnLabel() {
        if (match.isOurTurn()) {
            turnLabel.setText("Your turn");
        } else {
            turnLabel.setText("Opponent's turn");
        }
    }
}