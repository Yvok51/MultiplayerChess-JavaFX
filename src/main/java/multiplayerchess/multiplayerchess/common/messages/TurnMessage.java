package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;

import java.io.Serializable;

public final class TurnMessage extends ClientOngoingMatchMessage implements Serializable {

    static final long serialVersionUID = 0x1234567;
    // some boolean fields may be unnecesarry
    // public boolean castle;
    public final boolean isCapture;
    // public boolean check;
    public final PieceType pieceType;
    public final Position startingPosition;
    public final Position endingPosition;
    public final Color player;

    public TurnMessage(PieceType pieceType, Position startingPosition, Position endingPosition, Color player, boolean isCapture, String matchID) {
        super(matchID);
        this.pieceType = pieceType;
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.player = player;
        this.isCapture = isCapture;
    }
}
