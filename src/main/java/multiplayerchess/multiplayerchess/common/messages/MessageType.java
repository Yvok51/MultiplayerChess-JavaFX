package multiplayerchess.multiplayerchess.common.messages;

/**
 * The type of message
 */
public enum MessageType {
    START_GAME,
    JOIN_GAME,
    DISCONNECTED,
    CONNECTED,
    RESIGNED,
    TURN,
    HEARTBEAT
}
