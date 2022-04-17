package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

public final class StartGameReplyMessage extends ServerMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;
    public final boolean success;
    public final String matchID;

    public StartGameReplyMessage(boolean success, String matchID) {
        this.success = success;
        this.matchID = matchID;
    }
}
