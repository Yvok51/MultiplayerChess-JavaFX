package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Rook extends Piece {
    private static final Movement movement = new Movement(new MovementDirection[]{
            MovementDirection.UP,
            MovementDirection.DOWN,
            MovementDirection.LEFT,
            MovementDirection.RIGHT
    },
            true
    );
    private static final String IconFilename = "Rook.png";

    public Rook(Color color) {
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
        return PieceType.Rook;
    }
}
