package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

public final class OpponentConnectedMessage extends ServerMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    @Override
    public ServerMessageType getType() { return ServerMessageType.OPPONENT_CONNECTED; }
}
