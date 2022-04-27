package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * Message sent by the server to the client when the opponent has resigned.
 */
public final class OpponentResignedMessage extends ServerOngoingMatchMessage implements Serializable {

    static final long serialVersionUID = 0x1234567;

    public OpponentResignedMessage(String matchID) {
        super(matchID);
    }
}