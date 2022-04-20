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

    private static final String whiteIconFilename = basePiecePath + "white-knight.png";
    private static final String blackIconFilename = basePiecePath + "black-knight.png";

    public Knight(Color color) {
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
        return PieceType.Knight;
    }

}
