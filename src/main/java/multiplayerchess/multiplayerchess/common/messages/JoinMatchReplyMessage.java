package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

/**
 * The message sent by the server to the client when a player joins a match.
 */
public final class JoinMatchReplyMessage extends ServerMessage implements Serializable {
    static final long serialVersionUID = 0x123456;

    public final boolean success;
    public final String gameStateFEN;
    public final Player player;
    public final String matchID;

    public JoinMatchReplyMessage(boolean success, String gameStateFEN, Player player, String matchID) {
        this.success = success;
        this.gameStateFEN = gameStateFEN;
        this.player = player;
        this.matchID = matchID;
    }

    @Override
    public MessageType getType() { return MessageType.JOIN_GAME; }
}