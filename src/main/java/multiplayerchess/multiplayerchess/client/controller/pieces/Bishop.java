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
    private static final String IconFilename = "Bishop.png";

    public Bishop(Color color) {
        super(color);
    }

    @Override
    public Movement getMovement() {
        return movement;
    }

    @Override
    public String getIconFilename() {
        return IconFilename;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.Bishop;
    }
}
