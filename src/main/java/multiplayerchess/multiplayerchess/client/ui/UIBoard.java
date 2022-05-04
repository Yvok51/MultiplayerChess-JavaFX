package multiplayerchess.multiplayerchess.client.ui;

import javafx.scene.layout.GridPane;
import multiplayerchess.multiplayerchess.client.controller.Board;
import multiplayerchess.multiplayerchess.client.controller.Move;
import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.Player;

/**
 * The UIBoard class is responsible for displaying the board and the pieces.
 */
public class UIBoard extends GridPane  {

    private static final int CHESSBOARD_ROW_SIZE = 8;
    private static final int CHESSBOARD_COLUMN_SIZE = 8;

    private final UIBoardField[][] fields = new UIBoardField[CHESSBOARD_ROW_SIZE][CHESSBOARD_COLUMN_SIZE];
    private UIBoardField selectedField = null;
    private final Player player;
    private final ChessGameController controller;

    /**
     * The UI board constructor.
     * @param currentPlayer Which player are we
     * @param controller The controller of the game
     */
    public UIBoard(Player currentPlayer, ChessGameController controller) {
        super();

        this.player = currentPlayer;
        this.controller = controller;

        for (int x = 0; x < CHESSBOARD_ROW_SIZE; ++x) {
            for (int y = 0; y < CHESSBOARD_COLUMN_SIZE; ++y) {
                if ((x + y) % 2 != 0) {
                    fields[x][y] = new UIBoardField(Color.WHITE, x, y);
                } else {
                    fields[x][y] = new UIBoardField(Color.BLACK, x, y);
                }

                if (currentPlayer == Player.WHITE) {
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

    /**
     * Updates the UI board with the given board.
     * @param board the board to be updated to
     */
    public void setupBoard(Board board) {
        for (int x = 0; x < CHESSBOARD_ROW_SIZE; ++x) {
            for (int y = 0; y < CHESSBOARD_COLUMN_SIZE; ++y) {
                if (board.getPiece(x, y) != null) {
                    fields[x][y].setPiece(board.getPiece(x, y));
                }
                else {
                    fields[x][y].releasePiece();
                }
            }
        }
    }

    /**
     * Handles the click on a field.
     * Selects the field if it is not already selected. Deselects the field if it is already selected.
     * Moves the piece if we think a move is possible.
     * @param row the row of the field
     * @param column the column of the field
     */
    private void onFieldClick(int row, int column) {
        UIBoardField clickedField = fields[row][column];

        // We have a selected field
        if (selectedField != null && selectedField.isOccupied()) {
            // We clicked on a field with our own piece
            if (clickedField.getPieceColor() == selectedField.getPieceColor()) {
                if (clickedField == selectedField) {
                    this.deselectField();
                }
                else {
                    this.setSelectedField(clickedField); // change the focus
                }

                return;
            }

            boolean isCapture = fields[row][column].getPiece() != null;
            Move move = new Move(selectedField.getRow(), selectedField.getColumn(), row, column,
                    selectedField.getPiece().getPieceType(), isCapture);
            controller.movePiece(move);

        } else {
            if (fields[row][column].getPiece() != null && fields[row][column].getPieceColor() == player.getColor()) {
                this.setSelectedField(fields[row][column]);
            }
        }
    }

    /**
     * Sets the selected field.
     * @param selectedField the field to be selected
     */
    private void setSelectedField(UIBoardField selectedField) {
        this.deselectField();
        this.selectedField = selectedField;

        if (this.selectedField != null) {
            this.selectedField.setActive(true);
        }
    }

    /**
     * Deselects the currently selected field.
     */
    private void deselectField() {
        if (this.selectedField != null) {
            selectedField.setActive(false);
            selectedField = null;
        }
    }
}
