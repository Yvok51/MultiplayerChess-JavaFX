package multiplayerchess.multiplayerchess.common.messages;

/**
 * A message sent by the client to the server to indicate that the client is still alive.
 */
public final class HeartbeatReplyMessage extends ClientOngoingMatchMessage {
    static final long serialVersionUID = 0x1234567;

    @Override
    public MessageType getType() {
        return MessageType.HEARTBEAT;
    }
}
