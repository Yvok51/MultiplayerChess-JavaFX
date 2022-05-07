package multiplayerchess.multiplayerchess.server.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.Move;
import multiplayerchess.multiplayerchess.server.controller.rules.ChessRules;

import java.util.ArrayList;
import java.util.List;

public final class Rook extends Piece {
    private static final Movement movement = new Movement(new MovementDirection[]{
            MovementDirection.UP,
            MovementDirection.DOWN,
            MovementDirection.LEFT,
            MovementDirection.RIGHT
    },
            true
    );

    public Rook(Color color) {
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
        return PieceType.ROOK;
    }
}
