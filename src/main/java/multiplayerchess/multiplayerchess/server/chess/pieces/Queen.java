package multiplayerchess.multiplayerchess.server.chess.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.chess.Move;
import multiplayerchess.multiplayerchess.server.chess.rules.ChessRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the queen piece.
 */
public final class Queen extends Piece {
    private static final Movement movement = new Movement(new MovementDirection[]{
            MovementDirection.UP,
            MovementDirection.DOWN,
            MovementDirection.LEFT,
            MovementDirection.RIGHT,
            MovementDirection.UP_LEFT,
            MovementDirection.UP_RIGHT,
            MovementDirection.DOWN_LEFT,
            MovementDirection.DOWN_RIGHT
    },
            true
    );

    /**
     * The Queen constructor.
     *
     * @param color The color of the queen.
     */
    public Queen(Color color) {
        super(color);
    }

    @Override
    protected Movement getMovement() {
        return movement;
    }

    @Override
    protected Movement getCaptureMovement() {
        return movement;
    }

    @Override
    public List<Move> generateMoveList(Position start, boolean isCapture) {
        List<Move> moves = new ArrayList<>();

        Movement movement = isCapture ? getMovement() : getCaptureMovement();

        for (var direction : movement.directions) {
            int row = start.row + direction.row;
            int column = start.column + direction.column;

            while (row >= ChessRules.MinBoardRow && row <= ChessRules.MaxBoardRow
                    && column >= ChessRules.MinBoardColumn && column <= ChessRules.MaxBoardColumn) {
                moves.add(new Move(getType(), start, new Position(row, column), isCapture));

                row += direction.row;
                column += direction.column;
            }
        }

        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }
}
