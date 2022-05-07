package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * The message sent by the client to the server to join a match.
 */
public final class JoinMatchMessage extends ClientMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;
    public final String matchID;

    /**
     * Constructs a new JoinMatchMessage.
     *
     * @param matchID the ID of the match
     */
    public JoinMatchMessage(String matchID) {
        this.matchID = matchID;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN_GAME;
    }
}
