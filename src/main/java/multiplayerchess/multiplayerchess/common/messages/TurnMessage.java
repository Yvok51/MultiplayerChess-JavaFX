package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;

import java.io.Serializable;

/**
 * Message from the client to the server to tell the server the move made by the client.
 */
public final class TurnMessage extends ClientOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    public final boolean isCapture;
    public final PieceType pieceType;
    public final Position startingPosition;
    public final Position endingPosition;
    public final Color playerColor;

    /**
     * Constructs a new TurnMessage.
     *
     * @param pieceType        The type of piece that is being moved.
     * @param startingPosition The starting position of the piece.
     * @param endingPosition   The ending position of the piece.
     * @param playerColor      The color of the player who made the move.
     * @param isCapture        Whether the move is a capture.
     * @param matchID          The ID of the match.
     */
    public TurnMessage(PieceType pieceType, Position startingPosition, Position endingPosition,
                       Color playerColor, boolean isCapture, String matchID) {
        super(matchID);
        this.pieceType = pieceType;
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.playerColor = playerColor;
        this.isCapture = isCapture;
    }

    @Override
    public MessageType getType() {
        return MessageType.TURN;
    }
}
