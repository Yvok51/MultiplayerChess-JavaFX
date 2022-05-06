package multiplayerchess.multiplayerchess.common.networking;

import java.util.Queue;

/**
 * A message queue synchronized with a monitor and thus thread-safe.
 *
 * @param <T> The type of messages in the queue.
 */
public class MessageQueue<T> {
    private final Queue<T> queue;

    /**
     * Constructs a new message queue.
     */
    public MessageQueue() {
        queue = new java.util.LinkedList<>();
    }

    /**
     * Adds a message to the queue.
     *
     * @param message The message to add.
     */
    public synchronized void add(T message) {
        queue.add(message);
        this.notify();
    }

    /**
     * Removes and returns the next message in the queue.
     *
     * @return The next message in the queue.
     * @throws InterruptedException If the thread is interrupted while waiting for a message.
     */
    public synchronized T get() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }

        return queue.poll();
    }

    /**
     * Returns whether the queue is empty.
     *
     * @return Whether the queue is empty.
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
