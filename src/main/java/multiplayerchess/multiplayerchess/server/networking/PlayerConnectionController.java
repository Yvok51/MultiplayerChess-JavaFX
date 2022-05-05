package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.messages.*;
import multiplayerchess.multiplayerchess.common.networking.CallbackMap;
import multiplayerchess.multiplayerchess.common.networking.MessageQueue;
import multiplayerchess.multiplayerchess.common.networking.SocketMessageListener;
import multiplayerchess.multiplayerchess.common.networking.SocketMessageWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public class PlayerConnectionController implements AutoCloseable {

    public static PlayerConnectionController createController(Socket playerSocket) throws IOException {
        var controller = new PlayerConnectionController(playerSocket);

        MessageQueue<ServerMessage> queue = new MessageQueue<>();
        SocketMessageWriter<ServerMessage> writer = new SocketMessageWriter<>(playerSocket.getOutputStream(), queue);
        SocketMessageListener listener = new SocketMessageListener(
                playerSocket.getInputStream(), controller::handleMessage);

        controller.setWriter(writer, queue);
        controller.setListener(listener);

        return controller;
    }

    /**
     * Sends a message to opposite side.
     * @param message The message to send
     */
    public void sendMessage(ServerMessage message) {
        messageQueue.add(message);
    }

    /**
     * Clear the heartbeat occurred flag.
     */
    public void clearHeartbeat() { heartbeatOccurredFlag.set(false); }

    /**
     * Answer whether a heartbeat occurred in the last interval.
     * @return Whether a heartbeat occurred.
     */
    public boolean hasHeartbeatOccurred() { return heartbeatOccurredFlag.get(); }

    /**
     * Add a callback to be called when a message of the given type is received.
     * @param type The type of message to listen for
     * @param callback The callback to call when the message is received
     */
    public synchronized void addCallback(MessageType type, Consumer<Message> callback) {
        callbackMap.addCallback(type, callback);
    }

    /**
     * Remove a callback from the list of callbacks for the given type.
     * @param type The type of message to remove the callback from
     * @param callback The callback to remove
     */
    public synchronized void removeCallback(MessageType type, Consumer<Message> callback) {
        callbackMap.removeCallback(type, callback);
    }

    /**
     * Remove all callbacks for the given type.
     * @param type The type of message to remove all callbacks from
     */
    public synchronized void clearCallbacks(MessageType type) {
        callbackMap.clearCallbacks(type);
    }

    /**
     * Remove all callbacks
     */
    public synchronized void clearAllCallbacks() {
        callbackMap.clearAllCallbacks();
        // Bring back the default heartbeat callback
        callbackMap.addCallback(MessageType.HEARTBEAT, heartbeatCallback);
    }

    /**
     * Start the network controller.
     */
    public void start() {
        listener.start();
        writer.start();
    }

    public boolean isRunning() {
        return listener.isRunning() && writer.isRunning();
    }

    /**
     * Dispose of the network controller's resources.
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        listener.stopRunning();
        writer.stopRunning();
        if (!playerSocket.isClosed()) {
            playerSocket.close();
        }
    }

    /**
     * The handler for received messages.
     * A unified callback which the listener calls for each message received.
     * It calls the appropriate callbacks for the message type.
     * @param message The message received
     */
    private synchronized void handleMessage(Message message) {
        for (var callback : callbackMap.getCallbacks(message.getType())) {
            callback.accept(message);
        }
    }

    /**
     * Set the writer for the network controller.
     * @param writer The writer to use
     * @param queue The queue to use for sending messages to the writer
     */
    private void setWriter(SocketMessageWriter<ServerMessage> writer, MessageQueue<ServerMessage> queue) {
        this.writer = writer;
        this.messageQueue = queue;
    }

    /**
     * Set the listener for the network controller.
     * @param listener The listener to use
     */
    private void setListener(SocketMessageListener listener) {
        // Not in the constructor so that we can give it a callback from the network controller
        this.listener = listener;
    }

    private PlayerConnectionController(Socket playerSocket)
    {
        this.playerSocket = playerSocket;
        this.callbackMap = new CallbackMap<>();
        this.heartbeatOccurredFlag = new AtomicBoolean(false);
        this.heartbeatCallback = (message) -> heartbeatOccurredFlag.set(true);
        // default heartbeat callback
        callbackMap.addCallback(MessageType.HEARTBEAT, heartbeatCallback);
    }

    private final Socket playerSocket;
    private SocketMessageWriter<ServerMessage> writer;
    private SocketMessageListener listener;
    private MessageQueue<ServerMessage> messageQueue;

    private final AtomicBoolean heartbeatOccurredFlag;

    private final CallbackMap<MessageType, Consumer<Message>> callbackMap;
    private final Consumer<Message> heartbeatCallback;

}
