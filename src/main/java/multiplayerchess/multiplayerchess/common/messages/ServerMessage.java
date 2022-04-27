package multiplayerchess.multiplayerchess.common.messages;

import java.io.Serializable;

/**
 * The base class for all server messages.
 */
public abstract sealed class ServerMessage extends Message implements Serializable
        permits JoinMatchReplyMessage, StartGameReplyMessage, ServerOngoingMatchMessage {
    static final long serialVersionUID = 0x1234567;
}