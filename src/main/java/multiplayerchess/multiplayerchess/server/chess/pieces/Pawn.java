package multiplayerchess.multiplayerchess.server.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.chess.Move;
import multiplayerchess.multiplayerchess.server.chess.rules.ChessRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the pawn piece.
 */
public final class Pawn extends Piece {
    private static final Movement captureMovementWhite = new Movement(new MovementDirection[]{
            MovementDirection.UP_LEFT,
            MovementDirection.UP_RIGHT
    },
            false
    );
    private static final Movement captureMovementBlack = new Movement(new MovementDirection[]{
            MovementDirection.DOWN_LEFT,
            MovementDirection.DOWN_RIGHT
    },
            false
    );
    private static final Movement normalMovementWhite = new Movement(new MovementDirection[]{
            MovementDirection.UP
    },
            false
    );
    private static final Movement normalMovementBlack = new Movement(new MovementDirection[]{
            MovementDirection.DOWN
    },
            false
    );
    private static final Movement startMovementWhite = new Movement(new MovementDirection[]{
            MovementDirection.UP,
            MovementDirection.UP_DOUBLE
    },
            false
    );
    private static final Movement startMovementBlack = new Movement(new MovementDirection[]{
            MovementDirection.DOWN,
            MovementDirection.DOWN_DOUBLE
    },
            false
    );
    private final boolean hasMoved;

    /**
     * The Pawn constructor.
     *
     * @param color    The color of the pawn.
     * @param hasMoved Whether the pawn has moved yet.
     */
    public Pawn(Color color, boolean hasMoved) {
        super(color);
        this.hasMoved = hasMoved;
    }

    @Override
    protected Movement getMovement() {
        if (hasMoved) {
            return color == Color.WHITE ? normalMovementWhite : normalMovementBlack;
        } else {
            return color == Color.WHITE ? startMovementWhite : startMovementBlack;
        }
    }

    @Override
    protected Movement getCaptureMovement() {
        return color == Color.WHITE ? captureMovementWhite : captureMovementBlack;
    }

    @Override
    public List<Move> generateMoveList(Position start, boolean isCapture) {
        List<Move> moves = new ArrayList<>();

        Movement movement = isCapture ? getCaptureMovement() : getMovement();

        for (var direction : movement.directions) {
            int row = start.row + direction.row;
            int column = start.column + direction.column;

            if (row >= ChessRules.MinBoardRow && row <= ChessRules.MaxBoardRow
                    && column >= ChessRules.MinBoardColumn && column <= ChessRules.MaxBoardColumn) {
                moves.add(new Move(getType(), start, new Position(row, column), isCapture));
            }
        }

        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    @Override
    public Piece getMovedPiece() {
        return new Pawn(color, true);
    }
}
