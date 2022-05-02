package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.common.messages.ServerOngoingMatchMessage;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface for the network controller.
 * Responsible for handling all network communication
 */
public interface INetworkController extends AutoCloseable {
    /**
     * Send a request to start a match to the server.
     * @return The started match if the request was successful, otherwise an empty optional
     */
    Optional<Match> startMatch();

    /**
     * Sends a request to join a match to the server.
     * @param matchID The ID of the match to join
     * @return The joined match if the request was successful, otherwise an empty optional
     */
    Optional<Match> joinMatch(String matchID);

    /**
     * Sends a message to resign the match to the server.
     * Does not wait for reply
     * @param matchID The ID of the match
     */
    public void sendResign(String matchID);

    /**
     * Sends a turn message to the server.
     * @param pieceType The type of piece moved
     * @param startPosition The starting position of the piece
     * @param endPosition The ending position of the piece
     * @param color The color of the piece
     * @param isCapture Whether the move resulted in a capture
     * @param matchID The ID of the match
     * @return Whether the move was sent successfully
     */
    Optional<TurnReply> sendTurn(PieceType pieceType, Position startPosition,
                            Position endPosition, Color color, boolean isCapture, String matchID);
    Optional<ServerOngoingMatchMessage> receiveTurnReply();

    /**
     * Closes the connection to the server
     */
    @Override
    void close() throws IOException;
}
