package multiplayerchess.multiplayerchess.server.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.chess.Move;

import java.util.List;

/**
 * The base class for all pieces.
 */
public abstract class Piece {
    public final Color color;

    /**
     * The piece constructor
     *
     * @param color Color of the piece.
     */
    public Piece(Color color) {
        this.color = color;
    }

    /**
     * Gets the possible moves of the piece.
     *
     * @param start     Position of the piece.
     * @param isCapture Whether the moves are to be capture moves
     * @return Possible moves of the piece.
     */
    public abstract List<Move> generateMoveList(Position start, boolean isCapture);

    /**
     * Gets the type of the piece.
     *
     * @return Type of the piece.
     */
    public abstract PieceType getType();

    /**
     * Gets the version of the piece after it has been moved.
     *
     * @return Piece after it has been moved.
     */
    public Piece getMovedPiece() {
        return this;
    }

    /**
     * Gets the movement of the piece.
     *
     * @return Movement of the piece.
     */
    protected abstract Movement getMovement();

    /**
     * Gets the movement of the piece in which the piece can capture
     *
     * @return Movement of the piece in which the piece can capture
     */
    protected abstract Movement getCaptureMovement();
}
