package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.Player;

@FunctionalInterface
public interface PlayerConsumer<T> {
    void accept(T t, Player player);
}
