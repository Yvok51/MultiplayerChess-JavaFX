package multiplayerchess.multiplayerchess.common.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that manages callbacks for a specific type of event.
 *
 * @param <T> The type of event that the callback is for.
 * @param <V> The type of callback that is being managed.
 */
public class CallbackMap<T, V> {

    private final Map<T, List<V>> callbackMap;

    /**
     * Constructs a new CallbackMap.
     */
    public CallbackMap() {
        callbackMap = new HashMap<>();
    }

    /**
     * Adds a callback to be called when a message of the given type is received.
     *
     * @param type     The type of message to listen for
     * @param callback The callback to call when the message is received
     */
    public void addCallback(T type, V callback) {
        if (callbackMap.containsKey(type)) {
            callbackMap.get(type).add(callback);
        } else {
            List<V> callbacks = new ArrayList<>();
            callbacks.add(callback);
            callbackMap.put(type, callbacks);
        }
    }

    /**
     * Removes a callback from the list of callbacks for the given type.
     *
     * @param type     The type of message to remove the callback from
     * @param callback The callback to remove
     */
    public void removeCallback(T type, V callback) {
        var callbacks = callbackMap.get(type);
        if (callbacks != null) {
            callbacks.remove(callback);
        }
    }

    /**
     * Removes all callbacks for the given type.
     *
     * @param type The type of message to remove all callbacks from
     */
    public void clearCallbacks(T type) {
        callbackMap.remove(type);
    }

    /**
     * Removes all callbacks.
     */
    public void clearAllCallbacks() {
        callbackMap.clear();
    }

    /**
     * Gets a list of all callbacks for the given type.
     *
     * @param type The type of message to get the callbacks for
     * @return A list of callbacks for the given type
     */
    public List<V> getCallbacks(T type) {
        var callbacks = callbackMap.get(type);
        if (callbacks != null) {
            return callbacks;
        }

        return List.of();
    }
}
