package multiplayerchess.multiplayerchess.common.messages;

public final class HeartbeatMessage extends ServerOngoingMatchMessage{
    static final long serialVersionUID = 0x1234567;

    public HeartbeatMessage(String matchID) {
        super(matchID);
    }

    @Override
    public MessageType getType() {
        return MessageType.HEARTBEAT;
    }
}
