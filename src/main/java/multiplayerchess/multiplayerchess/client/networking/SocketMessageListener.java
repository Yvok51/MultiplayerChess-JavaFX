package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.messages.ServerMessage;
import multiplayerchess.multiplayerchess.common.messages.ServerOngoingMatchMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Listens for messages from the server.
 * Gets the input stream to listen on. Does not own the stream and as such does not close it.
 * For every message received, calls the given consumer with the message.
 */
public class SocketMessageListener extends Thread {
    private final InputStream inputStream;
    private final Consumer<ServerMessage> messageConsumer;
    private boolean running;

    /**
     * SocketMessageListener constructor.
     * @param inputStream the input stream to listen on.
     * @param callback the callback to call when a message is received.
     */
    public SocketMessageListener(InputStream inputStream, Consumer<ServerMessage> callback) {
        running = true;
        this.inputStream = inputStream;
        this.messageConsumer = callback;
    }

    /**
     * Listens for messages from the server.
     * For every message received, calls the given consumer with the message.
     */
    @Override
    public void run() {
        // Creates a new ObjectInputStream for every message received so that aren't problems with the headers.
        while (running) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                var message = (ServerMessage) objectInputStream.readObject();
                messageConsumer.accept(message);
            }
            catch (IOException | ClassNotFoundException e) {
                running = false;
            }
        }
    }
}
