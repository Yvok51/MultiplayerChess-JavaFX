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

    /**
     * Constructs a new JoinMatchReplyMessage.
     *
     * @param success      Whether the join was successful.
     * @param gameStateFEN The state of the game given by a FEN string.
     * @param player       The player we joined the match as.
     * @param matchID      The ID of the match we joined.
     */
    public JoinMatchReplyMessage(boolean success, String gameStateFEN, Player player, String matchID) {
        this.success = success;
        this.gameStateFEN = gameStateFEN;
        this.player = player;
        this.matchID = matchID;
    }

    @Override
    public MessageType getType() {
        return MessageType.JOIN_GAME;
    }
}