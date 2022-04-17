package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

public abstract sealed class Message implements Serializable
        permits ServerMessage, ClientMessage {
    static final long serialVersionUID = 0x1234567;
}
