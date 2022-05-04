package multiplayerchess.multiplayerchess.server.controller.pieces;

/**
 * Enum for the movement directions of a piece.
 */
public enum MovementDirection {

    UP(1, 0),
    DOWN(-1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),

    UP_LEFT(1, -1),
    UP_RIGHT(1, 1),
    DOWN_LEFT(-1, -1),
    DOWN_RIGHT(-1, 1),

    // Pawn first move
    UP_DOUBLE(2, 0),
    DOWN_DOUBLE(-2, 0),

    // Knight
    KNIGHT_LEFT_UP(1, -2),
    KNIGHT_UP_LEFT(2, -1),
    KNIGHT_UP_RIGHT(2, 1),
    KNIGHT_RIGHT_UP(1, 2),

    KNIGHT_RIGHT_DOWN(-1, 2),
    KNIGHT_DOWN_RIGHT(-2, 1),
    KNIGHT_DOWN_LEFT(-2, -1),
    KNIGHT_LEFT_DOWN(-1, -2);

    public final int row;
    public final int column;

    MovementDirection(int row, int column) {
        this.column = column;
        this.row = row;
    }
}
