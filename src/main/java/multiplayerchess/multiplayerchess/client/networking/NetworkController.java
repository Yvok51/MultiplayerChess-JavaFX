package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.MessageQueue;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.common.messages.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The NetworkController class is responsible for handling all network communication.
 */
public class NetworkController {

    /**
     * Factory method to construct a NetworkController.
     * @param host The hostname of the server
     * @param port The port of the server
     * @return A NetworkController
     * @throws IOException If the connection to the server fails
     */
    public static NetworkController connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        var controller = new NetworkController(socket);

        MessageQueue<ClientMessage> queue = new MessageQueue<>();
        SocketMessageWriter writer = new SocketMessageWriter(socket.getOutputStream(), queue);
        SocketMessageListener listener = new SocketMessageListener(
                socket.getInputStream(), controller::handleServerMessage);

        controller.setListener(listener);
        controller.setWriter(writer, queue);

        return controller;
    }

    /**
     * Add a callback to be called when a message of the given type is received.
     * @param type The type of message to listen for
     * @param callback The callback to call when the message is received
     */
    public synchronized void addCallback(ServerMessageType type, Consumer<ServerMessage> callback) {
        if (callbackMap.containsKey(type)) {
            callbackMap.get(type).add(callback);
        }
        else {
            List<Consumer<ServerMessage>> callbacks = new ArrayList<>();
            callbacks.add(callback);
            callbackMap.put(type, callbacks);
        }
    }

    /**
     * Remove a callback from the list of callbacks for the given type.
     * @param type The type of message to remove the callback from
     * @param callback The callback to remove
     */
    public synchronized void removeCallback(ServerMessageType type, Consumer<ServerMessage> callback) {
        var callbacks = callbackMap.get(type);
        if (callbacks != null) {
            callbacks.remove(callback);
        }
    }

    /**
     * Remove all callbacks for the given type.
     * @param type The type of message to remove all callbacks from
     */
    public synchronized void clearCallbacks(ServerMessageType type) {
        callbackMap.remove(type);
    }

    /**
     * Start the network controller.
     */
    public void start() {
        listener.start();
        writer.start();
    }

    /**
     * Send a request to start a match to the server.
     * @return The started match if the request was successful, otherwise an empty optional
     */
    public void requestNewMatch() {
        sendMessage(new StartGameMessage());
    }

    /**
     * Sends a request to join a match to the server.
     * @param matchID The ID of the match to join
     * @return The joined match if the request was successful, otherwise an empty optional
     */
    public void requestJoinMatch(String matchID) {
        sendMessage(new JoinMatchMessage(matchID));
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
    public void sendTurn(PieceType pieceType, Position startPosition, Position endPosition,
            Color color, boolean isCapture, String matchID) {
        sendMessage(new TurnMessage(pieceType, startPosition, endPosition, color, isCapture, matchID));
    }

    /**
     * Send a resign message to the server.
     * @param matchID The ID of the match
     */
    public void sendResign(String matchID) {
        sendMessage(new ResignMessage(matchID));
    }

    /**
     * Dispose of the network controller's resources.
     * @throws IOException
     */
    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    /**
     * Sends a message to the server.
     * @param message The message to send
     * @return Whether the message was sent successfully
     */
    private void sendMessage(ClientMessage message) {
        messageQueue.add(message);
    }

    /**
     * The handler for messages received from the server.
     * A unified callback which the listener calls for each message received.
     * It calls the appropriate callbacks for the message type.
     * @param message The message received
     */
    private synchronized void handleServerMessage(ServerMessage message) {
        var callbacks = callbackMap.get(message.getType());
        for (var callback : callbacks) {
            callback.accept(message);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     * @param socket The socket to use for communication
     */
    private NetworkController(Socket socket) {
        this.callbackMap = new HashMap<>();
        this.socket = socket;
    }

    /**
     * Set the writer for the network controller.
     * @param writer The writer to use
     * @param queue The queue to use for sending messages to the writer
     */
    private void setWriter(SocketMessageWriter writer, MessageQueue<ClientMessage> queue) {
        this.writer = writer;
        this.messageQueue = queue;
    }

    /**
     * Set the listener for the network controller.
     * @param listener The listener to use
     */
    private void setListener(SocketMessageListener listener) {
        this.listener = listener;
    }

    private final Socket socket;
    private SocketMessageWriter writer;
    private SocketMessageListener listener;
    private MessageQueue<ClientMessage> messageQueue;

    private Map<ServerMessageType, List<Consumer<ServerMessage>>> callbackMap;

}
