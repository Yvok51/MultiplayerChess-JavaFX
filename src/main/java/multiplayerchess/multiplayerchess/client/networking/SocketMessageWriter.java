package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.common.MessageQueue;
import multiplayerchess.multiplayerchess.common.messages.ClientMessage;

import java.io.ObjectOutputStream;

public class SocketMessageWriter extends Thread {
    private final ObjectOutputStream outputStream;
    private final MessageQueue<ClientMessage> messageQueue;
    boolean running;

    public SocketMessageWriter(ObjectOutputStream outputStream, MessageQueue<ClientMessage> messageQueue) {
        this.outputStream = outputStream;
        this.messageQueue = messageQueue;
        this.running = true;
    }

    public void run() {
        while (running) {
            ClientMessage message = messageQueue.get();
            if (message != null) {
                try {
                    outputStream.writeObject(message);
                }
                catch (Exception e) {
                    running = false;
                }
            }
        }
    }
}
