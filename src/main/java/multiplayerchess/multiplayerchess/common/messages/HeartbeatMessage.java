package multiplayerchess.multiplayerchess.common.messages;

/**
 * A message sent by the server to the client to ask whether the client is still connected.
 */
public final class HeartbeatMessage extends ServerOngoingMatchMessage {
    static final long serialVersionUID = 0x1234567;

    public HeartbeatMessage(String matchID) {
        super(matchID);
    }

    @Override
    public MessageType getType() {
        return MessageType.HEARTBEAT;
    }
}
