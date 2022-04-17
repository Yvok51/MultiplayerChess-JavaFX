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

public class MatchController {

    /**
     * Sends a message to the server.
     * @param pieceType The type of piece moved
     * @param startPosition The starting position of the piece
     * @param endPosition The ending position of the piece
     * @param color The color of the piece
     * @param isCapture Whether the move resulted in a capture
     * @return Whether the move was sent successfully
     */
    public boolean sendTurn(
            PieceType pieceType, Position startPosition, Position endPosition, Color color, boolean isCapture) {
        return sendMessage(new TurnMessage(pieceType, startPosition, endPosition, color, isCapture, matchID));
    }

    /**
     * Receives a message from the server.
     * @return The message received from the server or empty if there was an error
     */
    public Optional<ServerOngoingMatchMessage> receiveReply() {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            ServerOngoingMatchMessage reply = (ServerOngoingMatchMessage) inputStream.readObject();
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

    MatchController(Socket socket, String matchID) {
        this.socket = socket;
        this.matchID = matchID;
    }

    private final Socket socket;
    private final String matchID;
}
