package multiplayerchess.multiplayerchess.client.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

/**
 * Represents a bishop piece
 */
public class Bishop extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-bishop.png";
    private static final String blackIconFilename = basePiecePath + "black-bishop.png";

    /**
     * The Bishop constructor
     *
     * @param color the color of the bishop
     */
    public Bishop(Color color) {
        super(color);
    }

    @Override
    public String getIconFilename() {
        return color == Color.WHITE ? whiteIconFilename : blackIconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.BISHOP;
    }
}
