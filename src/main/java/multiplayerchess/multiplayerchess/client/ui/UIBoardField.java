package multiplayerchess.multiplayerchess.client.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import multiplayerchess.multiplayerchess.client.chess.pieces.Piece;
import multiplayerchess.multiplayerchess.common.Color;

/**
 * Represents a field on the UI board.
 */
public class UIBoardField extends Button {

    private final int row;
    private final int column;
    private final Color color;
    private Piece piece;

    /**
     * The UI board field constructor
     *
     * @param color  the color of the field
     * @param row    the row where the field is located
     * @param column the column where the field is located
     */
    public UIBoardField(Color color, int row, int column) {
        super();
        this.row = row;
        this.column = column;
        this.piece = null; //add piece later
        this.color = color;

        this.getStyleClass().add("chess-field");
        this.getStyleClass().add(this.getDefaultColorStyle());
    }

    /**
     * Sets the field to be highlighted as active or not
     *
     * @param active true if the field should be highlighted as active
     */
    public void setActive(boolean active) {
        // A bit dirty trick to change the color of the selected field
        if (active) {
            this.getStyleClass().removeAll(getDefaultColorStyle());
            this.getStyleClass().add("chess-field-active");
        } else {
            this.getStyleClass().removeAll("chess-field-active");
            this.getStyleClass().add(getDefaultColorStyle());
        }
    }

    /**
     * Releases the piece from the field
     *
     * @return the piece which was on the field
     */
    public Piece releasePiece() {
        Piece tmp = this.piece;
        setPiece(null);
        return tmp;
    }

    /**
     * Gets the color of the field
     *
     * @return the color of the field
     */
    public Color getPieceColor() {
        if (this.piece != null) {
            return piece.getColor();
        } else {
            return null;
        }
    }

    /**
     * Checks if the field is occupied
     *
     * @return true if the field is occupied
     */
    public boolean isOccupied() {
        return this.piece != null;
    }

    /**
     * Gets the piece on the field
     *
     * @return the piece on the field
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * Sets the piece on the field
     *
     * @param piece the piece to set
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        if (isOccupied()) {
            this.setGraphic(new ImageView(Utility.getImage(piece)));
        } else {
            this.setGraphic(new ImageView());
        }
    }

    /**
     * Gets the row of the field
     *
     * @return the row of the field
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column of the field
     *
     * @return the column of the field
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets the default css style which specifies the color of the field
     *
     * @return the default css style of the field
     */
    private String getDefaultColorStyle() {
        return color == Color.WHITE ? "chess-color-white" : "chess-color-black";
    }

}
