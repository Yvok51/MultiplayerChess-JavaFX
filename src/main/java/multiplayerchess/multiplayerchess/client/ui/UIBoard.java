package multiplayerchess.multiplayerchess.client.ui;

import javafx.scene.layout.GridPane;
import multiplayerchess.multiplayerchess.client.controller.Board;
import multiplayerchess.multiplayerchess.client.controller.Move;
import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.Player;

public class UIBoard extends GridPane  {

    private static final int CHESSBOARD_ROW_SIZE = 8;
    private static final int CHESSBOARD_COLUMN_SIZE = 8;

    private UIBoardField[][] fields = new UIBoardField[CHESSBOARD_ROW_SIZE][CHESSBOARD_COLUMN_SIZE];
    private UIBoardField selectedField = null;
    private final Player player;
    private final ChessGameController controller;

    public UIBoard(Player currentPlayer, ChessGameController controller) {
        super();

        this.player = currentPlayer;
        this.controller = controller;

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                if ((x + y) % 2 != 0) {
                    fields[x][y] = new UIBoardField(Color.White, x, y);
                } else {
                    fields[x][y] = new UIBoardField(Color.Black, x, y);
                }

                if (currentPlayer == Player.White) {
                    this.add(fields[x][y], y, CHESSBOARD_ROW_SIZE - 1 - x);
                } else {
                    this.add(fields[x][y], y, x);
                }

                final int xPosition = x;
                final int yPosition = y;
                fields[x][y].setOnAction(e -> onFieldClick(xPosition, yPosition));
            }
        }
    }

    public void setupBoard(Board board) {
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                if (board.getPiece(x, y) != null) {
                    fields[x][y].setPiece(board.getPiece(x, y));
                }
            }
        }
    }

    private void onFieldClick(int x, int y) {
        UIBoardField clickedField = fields[x][y];

        // if a piece is trying to get moved
        if (selectedField != null && selectedField.isOccupied()) {
            if (clickedField.getPieceColor() == selectedField.getPieceColor()) {
                this.deselectField();
                return;
            }

            boolean isCapture = fields[x][y].getPiece() != null;
            Move move = new Move(selectedField.getX(), selectedField.getY(), x, y,
                    selectedField.getPiece().getPieceType(), isCapture);
            controller.movePiece(move);

        } else {
            if (fields[x][y].getPiece() != null && fields[x][y].getPieceColor() == player.getColor()) {
                this.setSelectedField(fields[x][y]);
            }
        }
    }

    private void setSelectedField(UIBoardField selectedField) {
        this.deselectField();
        this.selectedField = selectedField;

        if (this.selectedField != null) {
            this.selectedField.setActive(true);
        }
    }

    private void deselectField() {
        if (this.selectedField != null) {
            selectedField.setActive(false);
            selectedField = null;
        }
    }
}
