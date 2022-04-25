package multiplayerchess.multiplayerchess.client.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import multiplayerchess.multiplayerchess.client.controller.pieces.Piece;
import multiplayerchess.multiplayerchess.common.Color;

public class UIBoardField extends Button {

    private int x;
    private int y;
    private Piece piece;
    private Color color;

    public UIBoardField(Color color, int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.piece = null; //add piece later
        this.color = color;

        this.getStyleClass().add("chess-field");
        this.getStyleClass().add(this.getDefaultColorStyle());
    }

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

    public Piece releasePiece() {
        Piece tmp = this.piece;
        setPiece(null);
        return tmp;
    }

    public Color getPieceColor() {
        if (this.piece != null) {
            return piece.getColor();
        } else {
            return null;
        }
    }

    public boolean isOccupied() {
        return this.piece != null;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (isOccupied()) {
            this.setGraphic(new ImageView(Utility.getImage(piece)));
        } else {
            this.setGraphic(new ImageView());
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private String getDefaultColorStyle() {
        return color == Color.WHITE ? "chess-color-white" : "chess-color-black";
    }

}