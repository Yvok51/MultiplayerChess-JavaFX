package multiplayerchess.multiplayerchess.client.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Rook extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-rook.png";
    private static final String blackIconFilename = basePiecePath + "black-rook.png";

    public Rook(Color color) {
        super(color);
    }

    @Override
    public String getIconFilename() {
        return color == Color.WHITE ? whiteIconFilename : blackIconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.ROOK;
    }
}
