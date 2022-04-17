package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

public final class OpponentDisconnectedMessage extends ServerOngoingMatchMessage implements Serializable {

    static final long serialVersionUID = 0x1234567;

    public OpponentDisconnectedMessage(String matchID) {
        super(matchID);
    }
}