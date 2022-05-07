package multiplayerchess.multiplayerchess.common;

/**
 * Simple interface for functions which take three parameters and return a value.
 */
@FunctionalInterface
public interface Func4<One, Two, Three, Four> {

    /**
     * Executes the function.
     *
     * @param one   The first parameter.
     * @param two   The second parameter.
     * @param three The third parameter.
     * @return The result of the function.
     */
    Four call(One one, Two two, Three three);
}