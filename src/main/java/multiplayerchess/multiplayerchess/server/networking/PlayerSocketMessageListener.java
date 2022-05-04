package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.messages.ClientMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Listens for messages from the server.
 * Gets the input stream to listen on. Does not own the stream and as such does not close it.
 * For every message received, calls the given consumer with the message.
 */
public class PlayerSocketMessageListener extends Thread {

    private final InputStream inputStream;
    private final PlayerConsumer<ClientMessage> messageConsumer;
    private final Player player;
    private boolean running;

    /**
     * SocketMessageListener constructor.
     * @param inputStream the input stream to listen on.
     * @param callback the callback to call when a message is received.
     */
    public PlayerSocketMessageListener(InputStream inputStream, PlayerConsumer<ClientMessage> callback, Player player) {
        running = true;
        this.player = player;
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
                var message = (ClientMessage) objectInputStream.readObject();
                messageConsumer.accept(message, player);
            }
            catch (IOException | ClassNotFoundException e) {
                running = false;
            }
        }
    }

    /**
     * Set the listener to stop listening for messages.
     */
    public void stopRunning() {
        running = false;
    }

    /**
     * Answers whether the listener is still running
     * @return Whether the listener is still running
     */
    public boolean isRunning() { return running; }
}

