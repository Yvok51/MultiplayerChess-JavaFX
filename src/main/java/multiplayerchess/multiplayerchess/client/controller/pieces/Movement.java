package multiplayerchess.multiplayerchess.client.controller.pieces;

public class Movement {
    public final MovementDirection[] directions;
    public final boolean sliding;

    public Movement(MovementDirection[] directions, boolean sliding) {
        this.directions = directions;
        this.sliding = sliding;
    }
}
