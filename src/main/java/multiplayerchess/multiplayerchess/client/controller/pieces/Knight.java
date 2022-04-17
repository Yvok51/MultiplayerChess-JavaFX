package multiplayerchess.multiplayerchess.client.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;

public class Knight extends Piece {
    private static final Movement movement = new Movement(new MovementDirection[]{
            MovementDirection.KNIGHT_DOWN_LEFT,
            MovementDirection.KNIGHT_DOWN_RIGHT,
            MovementDirection.KNIGHT_RIGHT_DOWN,
            MovementDirection.KNIGHT_LEFT_DOWN,
            MovementDirection.KNIGHT_UP_LEFT,
            MovementDirection.KNIGHT_UP_RIGHT,
            MovementDirection.KNIGHT_LEFT_UP,
            MovementDirection.KNIGHT_RIGHT_UP
    },
            false
    );
    private static final String IconFilename = "Knight.png";

    public Knight(Color color) {
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
        return PieceType.Knight;
    }

}
