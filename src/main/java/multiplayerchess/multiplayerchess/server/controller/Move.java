package multiplayerchess.multiplayerchess.server.controller;

import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Move {
    public final boolean isCapture;
    public final PieceType pieceType;
    public final Position startPosition;
    public final Position endPosition;

    public Move(PieceType pieceType, Position startPosition, Position endPosition, boolean isCapture) {
        this.pieceType = pieceType;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.isCapture = isCapture;
    }

    public int getRowDifference() {
        return endPosition.row - startPosition.row;
    }

    public int getColumnDifference() {
        return endPosition.column - startPosition.column;
    }

    public List<Position> getMovePath() {
        List<Position> path = new ArrayList<>();
        Position direction = getMovedDirection();

        int row = startPosition.row + direction.row;
        int column = startPosition.column + direction.column;

        while ((row != endPosition.row && column != endPosition.column) && row < ChessRules.RowCount && row > 0 && column < ChessRules.ColumnCount && column > 0) {
            path.add(new Position(row, column));
            row += direction.row;
            column += direction.column;
        }

        return path;
    }

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
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;
        Move move = (Move) o;
        return startPosition.equals(move.startPosition) && endPosition.equals(move.endPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, isCapture, pieceType);
    }
}
