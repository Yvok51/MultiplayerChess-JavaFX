package multiplayerchess.multiplayerchess.server.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.Move;

import java.util.List;

public abstract class Piece {
    public final Color color;

    /**
     * The piece constructor
     * @param color Color of the piece.
     */
    public Piece(Color color) {
        this.color = color;
    }

    /**
     * Get the possible moves of the piece.
     * @param start Position of the piece.
     * @param isCapture Whether the moves are to be capture moves
     * @return Possible moves of the piece.
     */
    public abstract List<Move> generateMoveList(Position start, boolean isCapture);

    /**
     * Get the type of the piece.
     * @return Type of the piece.
     */
    public abstract PieceType getType();

    /**
     * Get the version of the piece after it has been moved.
     * @return Piece after it has been moved.
     */
    public Piece getMovedPiece() {
        return this;
    }

    /**
     * Get the movement of the piece.
     * @return Movement of the piece.
     */
    abstract Movement getMovement();

    /**
     * Get the movement of the piece in which the piece can capture
     * @return Movement of the piece in which the piece can capture
     */
    abstract Movement getCaptureMovement();
}
