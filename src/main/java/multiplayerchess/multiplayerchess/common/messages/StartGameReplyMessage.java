package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

/**
 * Message sent by the server to the client to inform him of a started game.
 */
public final class StartGameReplyMessage extends ServerMessage implements Serializable {
    static final long serialVersionUID = 0x1234567;

    public final boolean success;
    public final String matchID;
    public final String startingFEN;
    public final Player player;

    public StartGameReplyMessage(boolean success, String matchID, String startingFEN, Player player) {
        this.success = success;
        this.matchID = matchID;
        this.startingFEN = startingFEN;
        this.player = player;
    }
}
