package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.common.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

/**
 * The NetworkController class is responsible for handling all network communication.
 */
public class NetworkController implements INetworkController {

    /**
     * Factory method to construct a NetworkController.
     * @param host The hostname of the server
     * @param port The port of the server
     * @return A NetworkController
     * @throws IOException If the connection to the server fails
     */
    public static NetworkController connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        return new NetworkController(socket);
    }

    /**
     * Send a request to start a match to the server.
     * @return The started match if the request was successful, otherwise an empty optional
     */
    public Optional<Match> StartMatch() {
        var success = sendStartMatch();
        if (!success) {
            return Optional.empty();
        }
        var optReply = recieveStartGameReply();
        if (optReply.isEmpty() || !optReply.get().success) {
            return Optional.empty();
        }
        var reply = optReply.get();
        return Optional.of(new Match(reply.startingFEN, reply.player, reply.matchID));
    }

    /**
     * Sends a request to join a match to the server.
     * @param matchID The ID of the match to join
     * @return The joined match if the request was successful, otherwise an empty optional
     */
    public Optional<Match> joinMatch(String matchID) {
        var success = sendJoinMatch(matchID);
        if (!success) {
            return Optional.empty();
        }
        var optReply = receiveJoinMatchReply();
        if (optReply.isEmpty() || !optReply.get().success) {
            return Optional.empty();
        }
        var reply = optReply.get();
        return Optional.of(new Match(reply.gameStateFEN, reply.player, reply.matchID));
    }

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
    public boolean sendTurn(PieceType pieceType, Position startPosition, Position endPosition,
            Color color, boolean isCapture, String matchID) {
        return sendMessage(new TurnMessage(pieceType, startPosition, endPosition, color, isCapture, matchID));
    }

    /**
     * Sends a request to join a match to the server.
     * @return Whether the request was sent successfully (does not comment on whether the request was accepted)
     */
    private boolean sendJoinMatch(String matchID) {
        return sendMessage(new JoinMatchMessage(matchID));
    }

    /**
     * Sends a request to start a match to the server.
     * @return Whether the request was sent successfully (does not comment on whether the request was accepted)
     */
    private boolean sendStartMatch() {
        return sendMessage(new StartGameMessage());
    }

    /**
     * Receives a StartGame reply message from the server.
     * @return The message received from the server or empty if there was an error
     */
    private Optional<StartGameReplyMessage> recieveStartGameReply() {
        var reply = receiveReply();
        if (reply.isEmpty()) {
            return Optional.empty();
        }
        StartGameReplyMessage startGameReply = (StartGameReplyMessage) reply.get();
        return Optional.of(startGameReply);
    }

    /**
     * Receives a JoinMatch reply message from the server.
     * @return The message received from the server or empty if there was an error
     */
    private Optional<JoinMatchReplyMessage> receiveJoinMatchReply() {
        var reply = receiveReply();
        if (reply.isEmpty()) {
            return Optional.empty();
        }
        JoinMatchReplyMessage joinMatchReply = (JoinMatchReplyMessage) reply.get();
        return Optional.of(joinMatchReply);
    }

    /**
     * Receives a turn reply message from the server.
     * @return The message received from the server or empty if there was an error
     */
    public Optional<ServerOngoingMatchMessage> receiveTurnReply() {
        var reply = receiveReply();
        if (reply.isEmpty()) {
            return Optional.empty();
        }
        ServerOngoingMatchMessage turnReply = (ServerOngoingMatchMessage) reply.get();
        return Optional.of(turnReply);
    }

    /**
     * Receives a message from the server.
     * @return The message received from the server or empty if there was an error
     */
    private Optional<ServerMessage> receiveReply() {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            ServerMessage reply = (ServerMessage) inputStream.readObject();
            return Optional.of(reply);
        }
        catch (IOException | ClassNotFoundException ignored) {
            return Optional.empty();
        }
    }

    /**
     * Sends a message to the server.
     * @param message The message to send
     * @return Whether the message was sent successfully
     */
    private boolean sendMessage(ClientMessage message) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            outputStream.writeObject(message);
            return true;
        }
        catch (IOException ignored) {
            return false;
        }
    }

    /**
     * Private constructor to prevent instantiation.
     * @param socket The socket to use for communication
     */
    NetworkController(Socket socket) {
        this.socket = socket;
    }

    private final Socket socket;
}
