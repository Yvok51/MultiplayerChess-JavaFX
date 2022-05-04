package multiplayerchess.multiplayerchess.common.messages;

public final class HeartbeatReplyMessage extends ClientOngoingMatchMessage {
    static final long serialVersionUID = 0x1234567;

    public HeartbeatReplyMessage(String matchID) {
        super(matchID);
    }

    @Override
    public MessageType getType() {
        return MessageType.HEARTBEAT;
    }
}
