package multiplayerchess.multiplayerchess.server;

/**
 * Print to standard output and error thread-safely.
 */
public class SafePrint {
    /**
     * Print to standard output thread-safely.
     *
     * @param s String to print.
     */
    public static synchronized void print(String s) {
        System.out.println(s);
    }

    /**
     * Print to standard error thread-safely.
     *
     * @param s String to print.
     */
    public static synchronized void printErr(String s) {
        System.out.println(s);
    }
}
