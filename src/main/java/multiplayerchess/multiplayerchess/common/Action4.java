package multiplayerchess.multiplayerchess.common;

@FunctionalInterface
public interface Action4<One, Two, Three, Four> {
    void call(One one, Two two, Three three, Four four);
}
