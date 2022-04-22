package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.common.Position;

public class Move {
    private final Position startPosition;
    private final Position endPosition;

    public Move(int oldX, int oldY, int newX, int newY) {
        this.startPosition = new Position(oldX, oldY);
        this.endPosition = new Position(newX, newY);
    }

    public Move(Position startPosition, Position endPosition) {
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
}
