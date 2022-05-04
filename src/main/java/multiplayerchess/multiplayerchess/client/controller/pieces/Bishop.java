package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Bishop extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-bishop.png";
    private static final String blackIconFilename = basePiecePath + "black-bishop.png";

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
