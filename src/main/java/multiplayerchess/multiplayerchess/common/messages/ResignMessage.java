package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * Message sent by the client to the server to resign the game.
 */
public final class ResignMessage extends ClientOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    public ResignMessage(String matchID) {
        super(matchID);
    }

    @Override
    public MessageType getType() { return MessageType.RESIGNED; }
}
