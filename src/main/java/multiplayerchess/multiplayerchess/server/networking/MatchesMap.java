package multiplayerchess.multiplayerchess.server.networking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MatchesMap {
    private final Map<String, MatchController> matches;

    public MatchesMap() {
        matches = new HashMap<>();
    }

    public synchronized MatchController addMatch(String matchID) {
        MatchController newMatch = new MatchController(matchID, this);
        matches.put(matchID, newMatch);
        return newMatch;
    }

    public synchronized boolean matchExists(String matchID) {
        return matches.containsKey(matchID);
    }

    public synchronized Optional<MatchController> getMatch(String matchID) {
        return Optional.ofNullable(matches.get(matchID));
    }

    /*/
    public synchronized Optional<MatchController> routeMessageHandler(ServerOngoingMatchMessage message) {
        var match = matches.getOrDefault(message.matchID, null);
        if (match != null) {
            return Optional.of(match);
        }
        else {
            return Optional.empty();
        }
    }
    /**/

    public synchronized void matchEnded(String matchID) {
        matches.remove(matchID);
    }
}
