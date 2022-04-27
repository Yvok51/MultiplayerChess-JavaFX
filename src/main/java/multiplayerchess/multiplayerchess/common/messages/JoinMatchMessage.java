package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * The message sent by the client to the server to join a match.
 */
public final class JoinMatchMessage extends ClientMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;
    public final String matchID;

    public JoinMatchMessage(String matchID) {
        this.matchID = matchID;
    }
}
