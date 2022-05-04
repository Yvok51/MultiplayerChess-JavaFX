package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * Message sent by the client to the server during an ongoing match.
 */
public abstract sealed class ClientOngoingMatchMessage extends ClientMessage implements Serializable
        permits TurnMessage, ResignMessage {
    static final long serialVersionUID = 0x1234567;

    public final String matchID;

    public ClientOngoingMatchMessage(String matchID) {
        this.matchID = matchID;
    }
}
