package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Knight extends Piece {
    private static final String whiteIconFilename = basePiecePath + "white-knight.png";
    private static final String blackIconFilename = basePiecePath + "black-knight.png";

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
