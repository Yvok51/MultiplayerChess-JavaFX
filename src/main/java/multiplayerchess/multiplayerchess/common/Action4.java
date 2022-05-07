package multiplayerchess.multiplayerchess.common;

/**
 * Simple interface for actions which have four parameters.
 */
@FunctionalInterface
public interface Action4<One, Two, Three, Four> {

    /**
     * Executes the action.
     *
     * @param one   The first parameter.
     * @param two   The second parameter.
     * @param three The third parameter.
     * @param four  The fourth parameter.
     */
    void call(One one, Two two, Three three, Four four);
}
