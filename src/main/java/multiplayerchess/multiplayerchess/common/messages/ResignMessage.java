package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

public final class ResignMessage extends ClientOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    public ResignMessage(String matchID) {
        super(matchID);
    }
}
