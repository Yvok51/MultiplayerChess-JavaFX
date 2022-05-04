package multiplayerchess.multiplayerchess.common.networking;

import java.util.Queue;

public class MessageQueue<T> {
    private final Queue<T> queue;

    public MessageQueue() {
        queue = new java.util.LinkedList<>();
    }

    public synchronized void add(T message) {
        queue.add(message);
        this.notify();
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
