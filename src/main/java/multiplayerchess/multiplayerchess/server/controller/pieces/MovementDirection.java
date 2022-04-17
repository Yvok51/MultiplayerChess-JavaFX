package multiplayerchess.multiplayerchess.server.controller.pieces;

import multiplayerchess.multiplayerchess.common.Position;

public enum MovementDirection {

    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),

    UP_LEFT(-1, 1),
    UP_RIGHT(1, 1),
    DOWN_LEFT(-1, -1),
    DOWN_RIGHT(1, -1),

    // Pawn first move
    UP_DOUBLE(0, 2),
    DOWN_DOUBLE(0, -2),

    // Knight
    KNIGHT_LEFT_UP(-2, 1),
    KNIGHT_UP_LEFT(-1, 2),
    KNIGHT_UP_RIGHT(1, 2),
    KNIGHT_RIGHT_UP(2, 1),

    KNIGHT_RIGHT_DOWN(2, -1),
    KNIGHT_DOWN_RIGHT(1, -2),
    KNIGHT_DOWN_LEFT(-1, -2),
    KNIGHT_LEFT_DOWN(-2, -1);

    public final int row;
    public final int column;

    MovementDirection(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public boolean equalsPosition(Position position) {
        return position != null && position.row == row && position.column == column;
    }

    public boolean equalsPosition(Position position, int directionMultiplier) {
        return position != null && position.row == directionMultiplier * row && position.column == directionMultiplier * column;
    }
}
