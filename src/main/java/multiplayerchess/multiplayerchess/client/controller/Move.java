package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;

/**
 * Represents a move in the game.
 */
public final class Move {
    private final Position startPosition;
    private final Position endPosition;
    private final PieceType pieceType;
    private final boolean isCapture;

    /**
     * The move constructor
     *
     * @param oldRow    the old row position of the piece
     * @param oldColumn the old column position of the piece
     * @param newRow    the new row position of the piece
     * @param newColumn the new column position of the piece
     * @param pieceType the type of piece that is moving
     * @param isCapture whether the move is a capture
     */
    public Move(int oldRow, int oldColumn, int newRow, int newColumn, PieceType pieceType, boolean isCapture) {
        this.pieceType = pieceType;
        this.isCapture = isCapture;
        this.startPosition = new Position(oldRow, oldColumn);
        this.endPosition = new Position(newRow, newColumn);
    }

    /**
     * The move constructor
     *
     * @param startPosition the start position of the move
     * @param endPosition   the end position of the move
     * @param pieceType     the type of piece that is moving
     * @param isCapture     whether the move is a capture
     */
    public Move(Position startPosition, Position endPosition, PieceType pieceType, boolean isCapture) {
        this.pieceType = pieceType;
        this.isCapture = isCapture;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * Returns the start position of the move
     *
     * @return the start position of the move
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * Returns the end position of the move
     *
     * @return the end position of the move
     */
    public Position getEndPosition() {
        return endPosition;
    }

    /**
     * Returns the type of piece that is moving
     *
     * @return the type of piece that is moving
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Returns whether the move is a capture
     *
     * @return whether the move is a capture
     */
    public boolean isCapture() {
        return isCapture;
    }
}
