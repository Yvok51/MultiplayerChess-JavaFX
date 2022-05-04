package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

public final class OpponentConnectedMessage extends ServerMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    @Override
    public MessageType getType() { return MessageType.CONNECTED; }
}
