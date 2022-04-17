package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

public final class TurnReplyMessage extends ServerOngoingMatchMessage implements Serializable {

    static final long serialVrsionUID = 0x123456;
    public final boolean success;
    public final String gameStateFEN;
    public final boolean gameOver;
    public final Player winner;

    public TurnReplyMessage(boolean success, String gameStateFEN, boolean gameOver, Player winner, String matchID) {
        super(matchID);
        this.success = success;
        this.gameOver = gameOver;
        this.gameStateFEN = gameStateFEN;
        this.winner = winner;
    }
}
