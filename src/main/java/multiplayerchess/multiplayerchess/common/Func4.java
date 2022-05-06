package multiplayerchess.multiplayerchess.common;

/**
 * Simple interface for functions which take three parameters and return a value.
 */
@FunctionalInterface
public interface Func4<One, Two, Three, Four> {
    Four call(One one, Two two, Three three);
}