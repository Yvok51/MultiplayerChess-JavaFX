package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * The base class for all messages.
 */
public abstract sealed class Message implements Serializable
        permits ServerMessage, ClientMessage {
    static final long serialVersionUID = 0x1234567;

    public abstract MessageType getType();
}
