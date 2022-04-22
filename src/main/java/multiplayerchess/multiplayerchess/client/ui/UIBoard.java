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

    public UIBoard(Player currentPlayer) {
        super();

        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                if ((x + y) % 2 != 0) {
                    fields[x][y] = new UIBoardField(Color.White, x, y);
                } else {
                    fields[x][y] = new UIBoardField(Color.Black, x, y);
                }

                if (currentPlayer == Player.White) {
                    this.add(fields[x][y], x, CHESSBOARD_COLUMN_SIZE - 1 - y);
                } else {
                    this.add(fields[x][y], CHESSBOARD_ROW_SIZE - 1 - x, y);
                }

                final int xPosition = x;
                final int yPosition = y;
                fields[x][y]
                        .setOnAction(e -> onFieldClick(xPosition, yPosition));
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
        if (selectedField != null && selectedField.isOccupied()
                && clickedField.getPieceColor() != selectedField.getPieceColor()) {

            Move move = new Move(selectedField.getX(), selectedField.getY(), x, y);

        } else {
            if (fields[x][y].getPiece() != null) {
                this.setSelectedField(fields[x][y]);
            }
        }
    }

    private void setSelectedField(UIBoardField selectedField) {
        this.deselectField();
        this.selectedField = selectedField;

        if (this.selectedField != null) {
            this.selectedField.getStyleClass().add("chess-field-active");
        }
    }

    private void deselectField() {
        if (this.selectedField != null) {
            selectedField.getStyleClass().removeAll("chess-field-active");
            selectedField = null;
        }
    }
}
