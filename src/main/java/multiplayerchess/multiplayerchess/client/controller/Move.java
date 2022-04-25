package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;

public final class Move {
    private final Position startPosition;
    private final Position endPosition;
    private final PieceType pieceType;
    private final boolean isCapture;

    public Move(int oldX, int oldY, int newX, int newY, PieceType pieceType, boolean isCapture) {
        this.pieceType = pieceType;
        this.isCapture = isCapture;
        this.startPosition = new Position(oldX, oldY);
        this.endPosition = new Position(newX, newY);
    }

    public Move(Position startPosition, Position endPosition, PieceType pieceType, boolean isCapture) {
        this.pieceType = pieceType;
        this.isCapture = isCapture;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getGapX() {
        return endPosition.row - startPosition.row;
    }

    public int getGapY() {
        return endPosition.column - startPosition.column;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public PieceType getPieceType() { return pieceType; }

    public boolean isCapture() { return isCapture; }
}
