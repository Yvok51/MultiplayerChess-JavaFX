package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * Message sent by the client to the server during an ongoing match.
 */
public abstract sealed class ClientOngoingMatchMessage extends ClientMessage implements Serializable
        permits TurnMessage, ResignMessage, HeartbeatReplyMessage, DisconnectMessage {
    static final long serialVersionUID = 0x1234567;

}
