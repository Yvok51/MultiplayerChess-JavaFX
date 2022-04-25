package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class King extends Piece {
    private static final Movement movement = new Movement(new MovementDirection[]{
            MovementDirection.UP,
            MovementDirection.DOWN,
            MovementDirection.LEFT,
            MovementDirection.RIGHT,
            MovementDirection.UP_LEFT,
            MovementDirection.UP_RIGHT,
            MovementDirection.DOWN_LEFT,
            MovementDirection.DOWN_RIGHT
    },
            false
    );

    private static final String whiteIconFilename = basePiecePath + "white-king.png";
    private static final String blackIconFilename = basePiecePath + "black-king.png";

    public King(Color color) {
        super(color);
    }

    @Override
    public Movement getMovement() {
        return movement;
    }

    @Override
    public String getIconFilename() {
        return color == Color.WHITE ? whiteIconFilename : blackIconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KING;
    }
}
