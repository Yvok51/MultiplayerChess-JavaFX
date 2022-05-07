package multiplayerchess.multiplayerchess.server.chess.pieces;

/**
 * The Movement class represents the movement of a piece - its direction and whether it slides
 */
public final class Movement {
    public final MovementDirection[] directions;
    public final boolean sliding;

    public Movement(MovementDirection[] directions, boolean sliding) {
        this.directions = directions;
        this.sliding = sliding;
    }
}
