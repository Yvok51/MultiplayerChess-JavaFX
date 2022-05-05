package multiplayerchess.multiplayerchess.common.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Writer for messages to opposite side.
 * Writes all messages that appear in the message queue.
 */
public class SocketMessageWriter<T> extends Thread {

    private final OutputStream outputStream;
    private final MessageQueue<T> messageQueue;
    boolean running;

    /**
     * The SocketMessageWriter constructor.
     *
     * @param outputStream The output stream to write to.
     * @param messageQueue The message queue to get the messages from.
     */
    public SocketMessageWriter(OutputStream outputStream, MessageQueue<T> messageQueue) {
        this.outputStream = outputStream;
        this.messageQueue = messageQueue;
        this.running = true;
    }

    /**
     * Continuously writes messages from the message queue to the stream.
     */
    @Override
    public void run() {
        // Creates a new ObjectOutputStream to write to the server for every message to avoid problems with the headers
        while (running) {
            try {
                T message = messageQueue.get();
                if (message != null) {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(message);
                }
            }
            catch (InterruptedException | IOException e) {
                running = false;
            }
        }
    }

    /**
     * Set the thread to stop
     */
    public void stopRunning() {
        running = false;
    }

    /**
     * Answers whether the writer is still running
     *
     * @return Whether the writer is still running
     */
    public boolean isRunning() {
        return running;
    }
}
