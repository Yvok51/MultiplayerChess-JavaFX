package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

/**
 * Message sent by the client to the server when the client disconnects.
 */
public final class DisconnectMessage extends ClientOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    public final Player disconnectingPlayer;

    /**
     * Constructs a new DisconnectMessage.
     *
     * @param matchID             The ID of the match
     * @param disconnectingPlayer The player who disconnected.
     */
    public DisconnectMessage(String matchID, Player disconnectingPlayer) {
        super(matchID);
        this.disconnectingPlayer = disconnectingPlayer;
    }

    @Override
    public MessageType getType() {
        return MessageType.DISCONNECTED;
    }
}