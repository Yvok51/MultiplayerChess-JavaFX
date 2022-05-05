package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.messages.JoinMatchMessage;
import multiplayerchess.multiplayerchess.common.messages.JoinMatchReplyMessage;
import multiplayerchess.multiplayerchess.common.messages.Message;
import multiplayerchess.multiplayerchess.common.messages.StartGameReplyMessage;

import java.util.Optional;
import java.util.Random;

/**
 * Class which handles starting a match.
 */
public class PreMatchController {
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERALS = "0123456789";
    private static final String MATCH_ID_AVAILABLE_CHARACTERS = LOWER_CASE + UPPER_CASE + NUMERALS;
    private static final int MATCH_ID_LENGTH = 1;
    private final PlayerConnectionController controller;
    private final MatchesMap controllerMap;

    /**
     * The StartMatchThread constructor.
     *
     * @param controller    The controller handling the player connection
     * @param controllerMap The map of match controllers to add the newly created match.
     */
    public PreMatchController(PlayerConnectionController controller, MatchesMap controllerMap) {
        this.controller = controller;
        this.controllerMap = controllerMap;
    }

    /**
     * Starts a match.
     * Generates a match id and sends it to the client.
     * Also adds the newly created match to the matches map and adds the client to the match.
     */
    public void startMatch(Message message) {
        controller.clearAllCallbacks();

        // TODO: Change the length of the generated match id according to the number of ongoing matches.
        RandomStringGenerator generator = new RandomStringGenerator(MATCH_ID_AVAILABLE_CHARACTERS);
        String potentialMatchID = generator.nextString(MATCH_ID_LENGTH);
        while (controllerMap.matchExists(potentialMatchID)) {
            potentialMatchID = generator.nextString(MATCH_ID_LENGTH);
        }

        MatchController createdMatch = controllerMap.addMatch(potentialMatchID);
        Player addedPlayer = createdMatch.addPlayer(controller);
        controller.sendMessage(
                new StartGameReplyMessage(true, potentialMatchID, createdMatch.getMatchFEN(), addedPlayer)
        );
        createdMatch.start();
    }

    /**
     * Looks whether the match ID is valid and if so, adds the client to the match.
     * Replies to the client with a JoinMatchReplyMessage.
     */
    public void joinMatch(Message message) {
        JoinMatchMessage joinMatchMessage = (JoinMatchMessage) message;
        controller.clearAllCallbacks();

        Optional<MatchController> matchToJoin = controllerMap.getMatch(joinMatchMessage.matchID);
        if (matchToJoin.isEmpty() || !matchToJoin.get().hasOpenSpot()) {
            controller.sendMessage(new JoinMatchReplyMessage(false, "", null, joinMatchMessage.matchID));
            return;
        }

        var matchController = matchToJoin.get();
        Player joinedAs = matchController.addPlayer(controller);

        controller.sendMessage(new JoinMatchReplyMessage(
                true, matchController.getMatchFEN(), joinedAs, matchController.getMatchID()));
    }

    /**
     * Class to generate a random string of a given length.
     */
    private static class RandomStringGenerator {
        private final String availableCharacters;
        private final Random random;

        /**
         * Constructor for the RandomStringGenerator.
         *
         * @param availableCharacters The characters to generate the string from.
         */
        public RandomStringGenerator(String availableCharacters) {
            this.availableCharacters = availableCharacters;
            random = new Random();
        }

        /**
         * Generates a random string of a given length.
         *
         * @param length The length of the string to generate.
         * @return The generated string.
         */
        public String nextString(int length) {
            if (length < 1) {
                return "";
            }

            StringBuilder builder = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                builder.append(nextChar());
            }

            return builder.toString();
        }

        /**
         * Generates a random character.
         *
         * @return The generated character.
         */
        private char nextChar() {
            int randomIndex = random.nextInt(availableCharacters.length());
            return availableCharacters.charAt(randomIndex);
        }
    }
}
