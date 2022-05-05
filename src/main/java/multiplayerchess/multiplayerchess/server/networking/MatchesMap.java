package multiplayerchess.multiplayerchess.server.networking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class which holds the match controllers of the ongoing matches.
 */
public class MatchesMap {
    private final Map<String, MatchController> matches;

    /**
     * MatchesMap constructor.
     */
    public MatchesMap() {
        matches = new HashMap<>();
    }

    /**
     * Adds a new match to the map.
     *
     * @param matchID The ID of the match.
     * @return The controller of the match.
     */
    public synchronized MatchController addMatch(String matchID) {
        MatchController newMatch = new MatchController(matchID, this);
        matches.put(matchID, newMatch);
        return newMatch;
    }

    /**
     * Answers whether the match exists
     *
     * @param matchID The ID of the match.
     * @return True if the match exists, false otherwise.
     */
    public synchronized boolean matchExists(String matchID) {
        return matches.containsKey(matchID);
    }

    /**
     * Gets the match controller of the given match ID.
     *
     * @param matchID The ID of the match.
     * @return The match controller with the given match ID.
     */
    public synchronized Optional<MatchController> getMatch(String matchID) {
        return Optional.ofNullable(matches.get(matchID));
    }

    /**
     * Disposes the match controller of the given match ID.
     *
     * @param matchID The ID of the match to dispose of.
     */
    public synchronized void matchEnded(String matchID) {
        matches.remove(matchID);
    }
}
