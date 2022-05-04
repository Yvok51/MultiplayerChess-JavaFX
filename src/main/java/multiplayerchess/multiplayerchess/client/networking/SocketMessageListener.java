package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.messages.ServerMessage;
import multiplayerchess.multiplayerchess.common.messages.ServerOngoingMatchMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class SocketMessageListener extends Thread {
    private final ObjectInputStream inputStream;
    private final Consumer<ServerOngoingMatchMessage> messageConsumer;
    private boolean running;

    public SocketMessageListener(ObjectInputStream inputStream, Consumer<ServerOngoingMatchMessage> callback) {
        running = true;
        this.inputStream = inputStream;
        this.messageConsumer = callback;
    }

    @Override
    public void run() {
        while (running) {
            try {
                var message = (ServerOngoingMatchMessage) inputStream.readObject();
                messageConsumer.accept(message);
            }
            catch (IOException | ClassNotFoundException e) {
                running = false;
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void end() {
        running = false;
    }
}
