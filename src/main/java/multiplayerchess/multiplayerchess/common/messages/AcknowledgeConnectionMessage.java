package multiplayerchess.multiplayerchess.common.messages;

/**
 * A message sent by the client to the server to acknowledge the connection
 */
public final class AcknowledgeConnectionMessage extends ClientMessage {
    static final long serialVersionUID = 0x1234567;

    @Override
    public MessageType getType() {
        return MessageType.JOIN_GAME;
    }
}
