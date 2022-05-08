package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

/**
 * Message sent by the client to the server to resign the game.
 */
public final class ResignMessage extends ClientOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    public final Player player;

    /**
     * Constructs a new ResignMessage.
     *
     * @param player  the player who resigned.
     */
    public ResignMessage(Player player) { this.player = player; }

    @Override
    public MessageType getType() {
        return MessageType.RESIGNED;
    }
}
