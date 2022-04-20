package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Bishop extends Piece {
    private static final Movement movement = new Movement(new MovementDirection[]{
            MovementDirection.UP_LEFT,
            MovementDirection.UP_RIGHT,
            MovementDirection.DOWN_LEFT,
            MovementDirection.DOWN_RIGHT
    },
            true
    );

    private static final String whiteIconFilename = basePiecePath + "white-bishop.png";
    private static final String blackIconFilename = basePiecePath + "black-bishop.png";

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public Movement getMovement() {
        return movement;
    }

    @Override
    public String getIconFilename(Color color) {
        return color == Color.White ? whiteIconFilename : blackIconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.Bishop;
    }
}
