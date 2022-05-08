package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * Message sent by the server to the client when the opponent disconnects.
 */
public final class OpponentDisconnectedMessage extends ServerOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    @Override
    public MessageType getType() {
        return MessageType.DISCONNECTED;
    }
}