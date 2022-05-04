package multiplayerchess.multiplayerchess.common.messages;

public enum ServerMessageType {
    START_GAME,
    JOIN_GAME,
    OPPONENT_DISCONNECTED,
    OPPONENT_CONNECTED,
    OPPONENT_RESIGNED,
    TURN,
}
