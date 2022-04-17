package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public abstract class Piece {
    protected final Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public abstract Movement getMovement();

    public abstract String getIconFilename();

    public abstract PieceType getPieceType();

    public Color getColor() {
        return color;
    }
}