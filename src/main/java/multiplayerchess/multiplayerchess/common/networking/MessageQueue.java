package multiplayerchess.multiplayerchess.common.networking;

import multiplayerchess.multiplayerchess.common.messages.Message;
import multiplayerchess.multiplayerchess.common.messages.ServerMessage;

import java.util.Queue;

public class MessageQueue<T> {
    private Queue<T> queue;

    public MessageQueue() {
        queue = new java.util.LinkedList<T>();
    }

    public synchronized void add(T message) {
        queue.add(message);
        this.notify();
    }

    public synchronized boolean hasMessage() {
        return !queue.isEmpty();
    }

    public synchronized T get() {
        while (queue.isEmpty()) {
            try {
                this.wait();
            }
            catch (InterruptedException ignored) {
            }
        }

        return queue.poll();
    }
}
