package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * Base message class sent by the server to the client during an ongoing match.
 */
public abstract sealed class ServerOngoingMatchMessage extends ServerMessage implements Serializable
        permits TurnReplyMessage, OpponentResignedMessage, OpponentDisconnectedMessage {
    static final long serialVersionUID = 0x1234567;

    public final String matchID;

    public ServerOngoingMatchMessage(String matchID) {
        this.matchID = matchID;
    }
}
