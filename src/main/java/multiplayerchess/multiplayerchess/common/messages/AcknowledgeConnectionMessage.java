package multiplayerchess.multiplayerchess.common.messages;

public final class AcknowledgeConnectionMessage extends ClientMessage {
    static final long serialVersionUID = 0x1234567;

    @Override
    public MessageType getType() {
        return MessageType.JOIN_GAME;
    }
}
