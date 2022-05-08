package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.common.messages.*;
import multiplayerchess.multiplayerchess.common.networking.CallbackMap;
import multiplayerchess.multiplayerchess.common.networking.MessageQueue;
import multiplayerchess.multiplayerchess.common.networking.SocketMessageListener;
import multiplayerchess.multiplayerchess.common.networking.SocketMessageWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * The NetworkController class is responsible for handling all network communication.
 */
public class NetworkController implements AutoCloseable {

    private final CallbackMap<MessageType, Consumer<Message>> callbackMap;
    private final Consumer<Message> heartbeatCallback;
    private SocketMessageWriter<ClientMessage> writer;
    private SocketMessageListener listener;
    private MessageQueue<ClientMessage> messageQueue;

    /**
     * Private constructor to prevent instantiation.
     */
    private NetworkController() {
        this.callbackMap = new CallbackMap<>();
        this.heartbeatCallback = (message) -> {
            var heartbeat = (HeartbeatMessage) message;
            sendMessage(new HeartbeatReplyMessage(heartbeat.matchID));
        };

        // Add default callback for heartbeat messages
        this.addCallback(MessageType.HEARTBEAT, this.heartbeatCallback);
    }

    /**
     * Factory method to construct a NetworkController.
     *
     * @param host The hostname of the server
     * @param port The port of the server
     * @return A NetworkController
     * @throws IOException If the connection to the server fails
     */
    public static NetworkController connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        var controller = new NetworkController();

        MessageQueue<ClientMessage> queue = new MessageQueue<>();
        SocketMessageWriter<ClientMessage> writer = new SocketMessageWriter<>(socket, queue, controller);
        SocketMessageListener listener = new SocketMessageListener(
                socket.getInputStream(), controller::handleServerMessage, controller);

        controller.setListener(listener);
        controller.setWriter(writer, queue);

        return controller;
    }

    /**
     * Sends a request to start a match to the server.
     */
    public void requestNewMatch() {
        sendMessage(new StartGameMessage());
    }

    /**
     * Sends a request to join a match to the server.
     *
     * @param matchID The ID of the match to join
     */
    public void requestJoinMatch(String matchID) {
        sendMessage(new JoinMatchMessage(matchID));
    }

    /**
     * Sends a turn message to the server.
     *
     * @param pieceType     The type of piece moved
     * @param startPosition The starting position of the piece
     * @param endPosition   The ending position of the piece
     * @param color         The color of the piece
     * @param isCapture     Whether the move resulted in a capture
     * @param matchID       The ID of the match
     */
    public void sendTurn(PieceType pieceType, Position startPosition, Position endPosition,
                         Color color, boolean isCapture, String matchID) {
        sendMessage(new TurnMessage(pieceType, startPosition, endPosition, color, isCapture, matchID));
    }

    /**
     * Send a resignation message to the server.
     *
     * @param matchID The ID of the match
     */
    public void sendResign(String matchID, Player player) {
        sendMessage(new ResignMessage(matchID, player));
    }

    public void sendConnectionAcknowledgement() {
        sendMessage(new AcknowledgeConnectionMessage());
    }

    /**
     * Adds a callback to be called when a message of the given type is received.
     *
     * @param type     The type of message to listen for
     * @param callback The callback to call when the message is received
     */
    public synchronized void addCallback(MessageType type, Consumer<Message> callback) {
        callbackMap.addCallback(type, callback);
    }

    /**
     * Removes a callback from the list of callbacks for the given type.
     *
     * @param type     The type of message to remove the callback from
     * @param callback The callback to remove
     */
    public synchronized void removeCallback(MessageType type, Consumer<Message> callback) {
        callbackMap.removeCallback(type, callback);
    }

    /**
     * Removes all callbacks for the given type.
     *
     * @param type The type of message to remove all callbacks from
     */
    public synchronized void clearCallbacks(MessageType type) {
        callbackMap.clearCallbacks(type);
    }

    public synchronized void clearAllCallbacks() {
        callbackMap.clearAllCallbacks();
        // Add back the default callback for heartbeat messages
        this.addCallback(MessageType.HEARTBEAT, this.heartbeatCallback);
    }

    /**
     * Starts the network controller.
     */
    public void start() {
        writer.start();
        listener.start();
    }

    /**
     * Disposes of the network controller's resources.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        listener.stopRunning();
        writer.stopRunning();
        writer.interrupt();
        listener.interrupt();
    }

    /**
     * Sends a message to the server.
     *
     * @param message The message to send
     */
    private void sendMessage(ClientMessage message) {
        messageQueue.add(message);
    }

    /**
     * The handler for messages received from the server.
     * A unified callback which the listener calls for each message received.
     * It calls the appropriate callbacks for the message type.
     *
     * @param message The message received
     */
    private synchronized void handleServerMessage(Message message) {
        for (var callback : callbackMap.getCallbacks(message.getType())) {
            callback.accept(message);
        }
    }

    /**
     * Sets the writer for the network controller.
     *
     * @param writer The writer to use
     * @param queue  The queue to use for sending messages to the writer
     */
    private void setWriter(SocketMessageWriter<ClientMessage> writer, MessageQueue<ClientMessage> queue) {
        this.writer = writer;
        this.messageQueue = queue;
    }

    /**
     * Sets the listener for the network controller.
     *
     * @param listener The listener to use
     */
    private void setListener(SocketMessageListener listener) {
        // Not in the constructor so that we can give it a callback from the network controller itself
        this.listener = listener;
    }

}
