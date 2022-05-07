package multiplayerchess.multiplayerchess.common.networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Writer for messages to opposite side.
 * Writes all messages that appear in the message queue.
 * Is the owner of the socket and the output stream, is responsible for closing the socket.
 */
public class SocketMessageWriter<T> extends Thread {

    private final Socket socket;
    private final MessageQueue<T> messageQueue;
    boolean running;

    /**
     * The SocketMessageWriter constructor.
     *
     * @param socket       The socket to write to.
     * @param messageQueue The message queue to get the messages from.
     */
    public SocketMessageWriter(Socket socket, MessageQueue<T> messageQueue) {
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.running = true;
    }

    /**
     * Continuously writes messages from the message queue to the stream.
     */
    @Override
    public void run() {
        // Creates a new ObjectOutputStream to write to the server for every message to avoid problems with the headers
        while (running || !messageQueue.isEmpty()) {
            try {
                T message = messageQueue.get();
                if (message != null) {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message);
                }
            }
            catch (InterruptedException | IOException e) {
                running = false;
            }
        }

        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        }
        catch (IOException ignored) {
        }
    }

    /**
     * Sets the thread to stop
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
