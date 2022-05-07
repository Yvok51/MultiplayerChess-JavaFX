package multiplayerchess.multiplayerchess.client.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Queen extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-queen.png";
    private static final String blackIconFilename = basePiecePath + "black-queen.png";

    public Queen(Color color) {
        super(color);
    }

    @Override
    public String getIconFilename() {
        return color == Color.WHITE ? whiteIconFilename : blackIconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.QUEEN;
    }
}
