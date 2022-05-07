package multiplayerchess.multiplayerchess.server.controller;

import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.rules.ChessRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a move in the game.
 */
public final class Move {
    public final boolean isCapture;
    public final PieceType pieceType;
    public final Position startPosition;
    public final Position endPosition;

    /**
     * The Move constructor.
     *
     * @param pieceType     the type of piece moved
     * @param startPosition The start position of the move
     * @param endPosition   The end position of the move
     * @param isCapture     Whether the move is a capture
     */
    public Move(PieceType pieceType, Position startPosition, Position endPosition, boolean isCapture) {
        this.pieceType = pieceType;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.isCapture = isCapture;
    }

    /**
     * Gets the row difference between the start and end position.
     *
     * @return the row difference between the start and end position
     */
    public int getRowDifference() {
        return endPosition.row - startPosition.row;
    }

    /**
     * Gets the column difference between the start and end position.
     *
     * @return the column difference between the start and end position
     */
    public int getColumnDifference() {
        return endPosition.column - startPosition.column;
    }

    public boolean isEnPassant() {
        return pieceType == PieceType.PAWN && Math.abs(getRowDifference()) == 2 && !isCapture;
    }

    /**
     * Gets the list of moves that the piece will go through.
     *
     * @return the list of moves that the piece will go through
     */
    public List<Position> getMovePath() {
        List<Position> path = new ArrayList<>();
        Position direction = getMovedDirection();

        int row = startPosition.row + direction.row;
        int column = startPosition.column + direction.column;

        while ((row != endPosition.row || column != endPosition.column) && row < ChessRules.RowCount && row >= 0 &&
                column < ChessRules.ColumnCount && column >= 0) {
            path.add(new Position(row, column));
            row += direction.row;
            column += direction.column;
        }

        return path;
    }

    /**
     * Gets the direction of the move i.e. from (7, 7) we get (1, 1) or (5, 0) we get (1, 0).
     * Does not change the knight movement direction.
     *
     * @return the direction of the move
     */
    private Position getMovedDirection() {
        int rowDifference = getRowDifference();
        int columnDifference = getColumnDifference();

        if (pieceType == PieceType.KNIGHT) {
            return new Position(rowDifference, columnDifference);
        }

        int rowDirection = rowDifference == 0 ? 0 : rowDifference / Math.abs(rowDifference);
        int columnDirection = columnDifference == 0 ? 0 : columnDifference / Math.abs(columnDifference);

        return new Position(rowDirection, columnDirection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        return startPosition.equals(move.startPosition) && endPosition.equals(move.endPosition)
                && pieceType == move.pieceType && isCapture == move.isCapture;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, isCapture, pieceType);
    }
}
