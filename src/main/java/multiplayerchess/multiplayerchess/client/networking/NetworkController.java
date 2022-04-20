package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.common.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class NetworkController {

    public static NetworkController connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        return new NetworkController(socket);
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
    public boolean sendTurn(
            PieceType pieceType, Position startPosition, Position endPosition,
            Color color, boolean isCapture, String matchID) {
        return sendMessage(new TurnMessage(pieceType, startPosition, endPosition, color, isCapture, matchID));
    }

    /**
     * Sends a request to join a match to the server.
     * @return Whether the request was sent successfully (does not comment on whether the request was accepted)
     */
    public boolean sendJoinMatch(String matchID) {
        return sendMessage(new JoinMatchMessage(matchID));
    }

    /**
     * Sends a request to start a match to the server.
     * @return Whether the request was sent successfully (does not comment on whether the request was accepted)
     */
    public boolean sendStartMatch() {
        return sendMessage(new StartGameMessage());
    }

    /**
     * Receives a StartGame reply message from the server.
     * @return The message received from the server or empty if there was an error
     */
    public Optional<StartGameReplyMessage> recieveStartGameReply() {
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
    public Optional<JoinMatchReplyMessage> receiveJoinMatchReply() {
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

    NetworkController(Socket socket) {
        this.socket = socket;
    }

    private final Socket socket;
}
