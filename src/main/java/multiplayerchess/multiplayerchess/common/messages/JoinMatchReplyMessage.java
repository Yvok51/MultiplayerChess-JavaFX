package multiplayerchess.multiplayerchess.common.messages;

import multiplayerchess.multiplayerchess.common.Player;

import java.io.Serializable;

public final class JoinMatchReplyMessage extends ServerMessage implements Serializable {

    static final long serialVrsionUID = 0x123456;
    public final boolean success;
    public final String gameStateFEN;
    public final Player playedColor;
    public final String matchID;

    public JoinMatchReplyMessage(boolean success, String gameStateFEN, Player playedColor, String matchID) {
        this.success = success;
        this.gameStateFEN = gameStateFEN;
        this.playedColor = playedColor;
        this.matchID = matchID;
    }
}