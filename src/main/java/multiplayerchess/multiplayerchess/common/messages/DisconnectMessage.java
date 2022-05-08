package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

/**
 * Message sent by the client to the server when the client disconnects.
 */
public final class DisconnectMessage extends ClientOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    @Override
    public MessageType getType() {
        return MessageType.DISCONNECTED;
    }
}