package multiplayerchess.multiplayerchess.common;

/**
 * Simple interface for actions which have four parameters.
 */
@FunctionalInterface
public interface Action4<One, Two, Three, Four> {
    void call(One one, Two two, Three three, Four four);
}
