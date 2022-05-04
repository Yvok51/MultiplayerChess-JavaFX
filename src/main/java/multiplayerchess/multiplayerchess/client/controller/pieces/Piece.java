package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public abstract class Piece {
    protected final static String basePiecePath = "/multiplayerchess/multiplayerchess/images/";

    protected final Color color;

    /**
     * Piece constructor
     * @param color the color of the piece
     */
    public Piece(Color color) {
        this.color = color;
    }

    /**
     * Returns the path to the image of the piece
     * @return the path to the image of the piece
     */
    public abstract String getIconFilename();

    /**
     * Returns the type of the piece
     * @return the type of the piece
     */
    public abstract PieceType getPieceType();

    /**
     * Returns the color of the piece
     * @return the color of the piece
     */
    public Color getColor() {
        return color;
    }
}
