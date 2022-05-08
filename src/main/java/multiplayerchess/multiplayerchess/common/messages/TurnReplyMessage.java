package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

/**
 * Message sent by the server to the client to inform about the result of a previously sent turn request.
 */
public final class TurnReplyMessage extends ServerOngoingMatchMessage implements Serializable {
    static final long serialVersionUID = 0x123456;

    public final boolean success;
    public final String gameStateFEN;
    public final boolean gameOver;
    public final Player winner;

    /**
     * Constructs a new TurnReplyMessage.
     *
     * @param success      Whether the turn was successful.
     * @param gameStateFEN The game state FEN after the turn.
     * @param gameOver     Whether the game is over.
     * @param winner       The winner of the game in case the game has ended.
     */
    public TurnReplyMessage(boolean success, String gameStateFEN, boolean gameOver, Player winner) {
        this.success = success;
        this.gameOver = gameOver;
        this.gameStateFEN = gameStateFEN;
        this.winner = winner;
    }

    @Override
    public MessageType getType() {
        return MessageType.TURN;
    }
}
