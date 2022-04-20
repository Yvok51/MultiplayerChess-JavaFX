package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Pawn extends Piece {

    private static final String whiteIconFilename = basePiecePath + "white-pawn.png";
    private static final String blackIconFilename = basePiecePath + "black-pawn.png";

    private final boolean hasMoved;

    public Pawn(Color color) {
        super(color);
        this.hasMoved = false;
    }

    @Override
    public Movement getMovement() {
        if (color == Color.Black) {
            return new Movement(new MovementDirection[]{MovementDirection.DOWN}, false);
        } else {
            return new Movement(new MovementDirection[]{MovementDirection.UP}, false);
        }
    }

    @Override
    public String getIconFilename(Color color) {
        return color == Color.White ? whiteIconFilename : blackIconFilename;
    }
    @Override
    public PieceType getPieceType() {
        return PieceType.Pawn;
    }
}
