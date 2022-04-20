package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

public final class JoinMatchMessage extends ClientMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;
    public final String matchID;

    public JoinMatchMessage(String matchID) {
        this.matchID = matchID;
    }
}
