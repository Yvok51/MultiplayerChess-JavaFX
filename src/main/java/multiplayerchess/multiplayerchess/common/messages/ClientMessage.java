package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

public abstract sealed class ClientMessage extends Message implements Serializable
        permits ClientOngoingMatchMessage, JoinMatchMessage, StartGameMessage {
    static final long serialVersionUID = 0x1234567;
}
