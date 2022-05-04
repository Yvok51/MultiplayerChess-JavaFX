package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Pawn extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-pawn.png";
    private static final String blackIconFilename = basePiecePath + "black-pawn.png";

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
