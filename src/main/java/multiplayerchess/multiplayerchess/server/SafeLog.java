package multiplayerchess.multiplayerchess.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logs thread-safely.
 * If more games are played simultaneously than this would become a bottleneck, but for debugging purposes it is fine.
 */
public class SafeLog {

    private static final Logger LOGGER = Logger.getLogger(SafeLog.class.getName());

    static {
        LOGGER.addHandler(new ConsoleHandler());
    }

    /**
     * Prints to standard output thread-safely.
     *
     * @param s String to print.
     */
    public static synchronized void log(Level level, String s) {
        LOGGER.log(level, s);
    }
}
