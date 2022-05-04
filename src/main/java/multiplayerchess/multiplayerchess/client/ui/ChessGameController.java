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
import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.client.controller.Move;
import multiplayerchess.multiplayerchess.client.controller.Winner;
import multiplayerchess.multiplayerchess.client.networking.NetworkController;
import multiplayerchess.multiplayerchess.common.messages.ServerMessage;
import multiplayerchess.multiplayerchess.common.messages.MessageType;
import multiplayerchess.multiplayerchess.common.messages.TurnReplyMessage;

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

    @FXML
    private Pane popupPane;
    @FXML
    private Label popupLabel;
    @FXML
    private Button popupButton;

    private Match match;
    private NetworkController networkController;

    private Stage stage;
    private UIBoard board;

    // private UIPopup popup;

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
    public void setupController(Match match, NetworkController networkController, Stage stage) {
        this.stage = stage;
        this.networkController = networkController;
        this.match = match;

        networkController.addCallback(MessageType.TURN, this::turnHandler);
        networkController.addCallback(MessageType.RESIGNED, this::opponentResignedHandler);
        networkController.addCallback(MessageType.DISCONNECTED, this::opponentDisconnectedHandler);
        networkController.addCallback(MessageType.CONNECTED, this::opponentJoinedHandler);

        board = new UIBoard(match.getPlayer(), this);
        board.setupBoard(match.getBoard());

        chessGamePane.getChildren().add(board);

        AnchorPane.setTopAnchor(board, 40.0);
        AnchorPane.setLeftAnchor(board, 160.0);

        this.setWaitingForOpponent();

        popupButton.setOnAction(event -> {
            popupPane.setVisible(false);
            // Go back to main menu
            try {
                FXMLLoader loader = Utility.loadNewScene(stage, MainMenuController.getFXMLFile());
                MainMenuController controller = loader.getController();
                controller.setupController(stage);
            } catch (IOException ex) {
                ex.printStackTrace();
                Platform.exit();
            }
        });

        this.newTurn();
        matchIDLabel.setText("Match ID: " + match.getMatchID());

    }

    public void opponentResignedHandler(ServerMessage message) {
        Platform.runLater(() -> {
            endMatch(Winner.getWinnerFromPlayer(match.getPlayer()), "Opponent resigned");
        });
    }

    public void opponentDisconnectedHandler(ServerMessage message) {
        Platform.runLater(() -> {
            endMatch(Winner.getWinnerFromPlayer(match.getPlayer()), "Opponent disconnected");
        });
    }

    public void turnHandler(ServerMessage message) {
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
        }
        else {
            Platform.runLater(() -> {
                errorText.setText("Invalid move");
            });
        }
    }

    public void opponentJoinedHandler(ServerMessage message) {
        popupPane.setVisible(false);
        this.setDisable(false);
    }

    /**
     * Called when the user clicks on a square in order to move a selected piece.
     * Calls the server to perform the move and if everything is successful, updates the board,
     * otherwise displays an error message.
     * @param move The move to make.
     */
    public void movePiece(Move move) {
        networkController.sendTurn(move.getPieceType(), move.getStartPosition(),
                move.getEndPosition(), match.getPlayer().getColor(), move.isCapture(), match.getMatchID());
    }

    public void onResignAndQuit() {
        networkController.sendResign(match.getMatchID());
        endMatch(Winner.getWinnerFromPlayer(match.getPlayer().opposite()), "Resignation");
    }

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
        popupLabel.setText(winText);
        popupPane.setVisible(true);

        popupButton.setVisible(true);
        popupButton.setDisable(false);

        this.setDisable(true);

    }

    private String getWinnerText(Winner winner) {
        String winText = winner.toString();
        if (winner != Winner.NONE) {
            winText += " Won";
        }
        return winText;
    }

    private void setDisable(boolean disable) {
        board.setDisable(disable);
        resignButton.setDisable(disable);
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