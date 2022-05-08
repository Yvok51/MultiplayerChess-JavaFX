package multiplayerchess.multiplayerchess.common.networking;

import multiplayerchess.multiplayerchess.common.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Listens for messages from the server.
 * Gets the input stream to listen on. Does not own the stream and as such does not close it.
 * For every message received, calls the given consumer with the message.
 */
public class SocketMessageListener extends Thread {
    private final InputStream inputStream;
    private final Consumer<Message> messageConsumer;
    private AtomicBoolean running;
    private AutoCloseable parent;

    /**
     * SocketMessageListener constructor.
     *
     * @param inputStream the input stream to listen on.
     * @param callback    the callback to call when a message is received.
     */
    public SocketMessageListener(InputStream inputStream, Consumer<Message> callback, AutoCloseable closeable) {
        running = new AtomicBoolean(true);
        this.inputStream = inputStream;
        this.messageConsumer = callback;
        this.parent = closeable;
    }

    /**
     * Listens for messages from the server.
     * For every message received, calls the given consumer with the message.
     */
    @Override
    public void run() {
        // Creates a new ObjectInputStream for every message received so that aren't problems with the headers.
        while (running.get()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                var message = (Message) objectInputStream.readObject();
                messageConsumer.accept(message);
            }
            catch (IOException | ClassNotFoundException | ClassCastException e) {
                running.set(false);
                try {
                    parent.close();
                }
                catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Sets the listener to stop listening for messages.
     */
    public void stopRunning() {
        running.set(false);
    }

    /**
     * Answers whether the listener is still running.
     *
     * @return Whether the listener is still running.
     */
    public boolean isRunning() {
        return running.get();
    }
}
