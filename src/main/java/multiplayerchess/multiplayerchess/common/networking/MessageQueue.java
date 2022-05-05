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

    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }

        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
