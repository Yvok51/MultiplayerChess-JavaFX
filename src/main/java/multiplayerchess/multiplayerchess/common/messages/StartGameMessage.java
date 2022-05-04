package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * Message sent by the client to the server to start the game.
 */
public final class StartGameMessage extends ClientMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    @Override
    public MessageType getType() { return MessageType.START_GAME; }
}
