package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.MessageQueue;
import multiplayerchess.multiplayerchess.common.messages.ClientMessage;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Writer for messages to the server.
 * Writes all messages that appear in the message queue to the server.
 */
public class SocketMessageWriter extends Thread {

    private final OutputStream outputStream;
    private final MessageQueue<ClientMessage> messageQueue;
    boolean running;

    /**
     * The SocketMessageWriter constructor.
     * @param outputStream The output stream to write to.
     * @param messageQueue The message queue to get the messages from.
     */
    public SocketMessageWriter(OutputStream outputStream, MessageQueue<ClientMessage> messageQueue) {
        this.outputStream = outputStream;
        this.messageQueue = messageQueue;
        this.running = true;
    }

    /**
     * Continuously writes messages from the message queue to the server.
     */
    @Override
    public void run() {
        // Creates a new ObjectOutputStream to write to the server for every message to avoid problems with the headers
        while (running) {
            ClientMessage message = messageQueue.get();
            if (message != null) {
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(message);
                }
                catch (Exception e) {
                    running = false;
                }
            }
        }
    }
}
