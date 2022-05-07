package multiplayerchess.multiplayerchess.client.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

/**
 * Represents a pawn piece.
 */
public class Pawn extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-pawn.png";
    private static final String blackIconFilename = basePiecePath + "black-pawn.png";

    /**
     * The Pawn constructor.
     *
     * @param color The color of the pawn.
     */
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public String getIconFilename() {
        return color == Color.WHITE ? whiteIconFilename : blackIconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }
}
