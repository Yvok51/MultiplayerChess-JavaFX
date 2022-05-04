package multiplayerchess.multiplayerchess.common.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallbackMap<T, V> {

    public CallbackMap() {
        callbackMap = new HashMap<>();
    }

    /**
     * Add a callback to be called when a message of the given type is received.
     * @param type The type of message to listen for
     * @param callback The callback to call when the message is received
     */
    public void addCallback(T type, V callback) {
        if (callbackMap.containsKey(type)) {
            callbackMap.get(type).add(callback);
        }
        else {
            List<V> callbacks = new ArrayList<>();
            callbacks.add(callback);
            callbackMap.put(type, callbacks);
        }
    }

    /**
     * Remove a callback from the list of callbacks for the given type.
     * @param type The type of message to remove the callback from
     * @param callback The callback to remove
     */
    public void removeCallback(T type, V callback) {
        var callbacks = callbackMap.get(type);
        if (callbacks != null) {
            callbacks.remove(callback);
        }
    }

    /**
     * Remove all callbacks for the given type.
     * @param type The type of message to remove all callbacks from
     */
    public void clearCallbacks(T type) {
        callbackMap.remove(type);
    }

    public List<V> getCallbacks(T type) {
        var callbacks = callbackMap.get(type);
        if (callbacks != null) {
            return callbacks;
        }

        return List.of();
    }

    private final Map<T, List<V>> callbackMap;
}
