package multiplayerchess.multiplayerchess.common.messages;

/**
 * A message sent by the client to the server to indicate that the client is still alive.
 */
public final class HeartbeatReplyMessage extends ClientOngoingMatchMessage {
    static final long serialVersionUID = 0x1234567;

    /**
     * Constructs a new HeartbeatReplyMessage.
     *
     * @param matchID the ID of the match
     */
    public HeartbeatReplyMessage(String matchID) {
        super(matchID);
    }

    @Override
    public MessageType getType() {
        return MessageType.HEARTBEAT;
    }
}
