package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.messages.OpponentDisconnectedMessage;
import multiplayerchess.multiplayerchess.common.messages.OpponentResignedMessage;
import multiplayerchess.multiplayerchess.common.messages.TurnReplyMessage;

public final class TurnReply {
    public final TurnReplyStatus status;
    public final String matchID;
    public final String gameStateFEN;
    public final boolean gameOver;
    public final Player winner;

    public TurnReply(OpponentResignedMessage message) {
        status = TurnReplyStatus.OPPONENT_RESIGNED;
        matchID = message.matchID;
        gameStateFEN = null;
        gameOver = false;
        winner = null;
    }

    public TurnReply(OpponentDisconnectedMessage message) {
        status = TurnReplyStatus.OPPONENT_DISCONNECTED;
        matchID = message.matchID;
        gameStateFEN = null;
        gameOver = false;
        winner = null;
    }

    public TurnReply(TurnReplyMessage message) {
        status = message.success ? TurnReplyStatus.TURN_ACCEPTED : TurnReplyStatus.TURN_REJECTED;
        matchID = message.matchID;
        gameStateFEN = message.gameStateFEN;
        gameOver = message.gameOver;
        winner = message.winner;
    }
}
