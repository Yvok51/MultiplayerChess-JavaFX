package multiplayerchess.multiplayerchess.client.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

/**
 * Represents the knight piece
 */
public class Knight extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-knight.png";
    private static final String blackIconFilename = basePiecePath + "black-knight.png";

    /**
     * The Knight constructor
     *
     * @param color the color of the knight
     */
    public Knight(Color color) {
        super(color);
    }

    @Override
    public String getIconFilename() {
        return color == Color.WHITE ? whiteIconFilename : blackIconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KNIGHT;
    }

}
