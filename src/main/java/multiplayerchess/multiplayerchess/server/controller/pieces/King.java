package multiplayerchess.multiplayerchess.server.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.ChessRules;
import multiplayerchess.multiplayerchess.server.controller.Move;

import java.util.ArrayList;
import java.util.List;

public final class King extends Piece {
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
            false
    );
    private static final String IconFilename = "King.png";

    public King(Color color) {
        super(color);
    }

    @Override
    public Movement getMovement() {
        return movement;
    }

    @Override
    public Movement getCaptureMovement() {
        return movement;
    }

    @Override
    public List<Move> generateMoveList(Position start, boolean isCapture) {
        List<Move> moves = new ArrayList<>();

        Movement movement = isCapture ? getMovement() : getCaptureMovement();

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
        return PieceType.KING;
    }
}
