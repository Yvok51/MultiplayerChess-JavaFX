package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.messages.StartGameReplyMessage;
import multiplayerchess.multiplayerchess.server.SafePrint;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Class which handles starting a match.
 */
public class StartMatchThread implements Runnable {
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERALS = "0123456789";
    private static final String MATCH_ID_AVAILABLE_CHARACTERS = LOWER_CASE + UPPER_CASE + NUMERALS;
    private static final int MATCH_ID_LENGTH = 8;
    private final Socket socket;
    MatchesMap controllers;
    RandomStringGenerator generator;

    /**
     * The StartMatchThread constructor.
     * @param socket The socket to which the client is connected.
     * @param controllers The map of match controllers to add the newly created match.
     */
    public StartMatchThread(Socket socket, MatchesMap controllers) {
        this.socket = socket;
        this.controllers = controllers;
        this.generator = new RandomStringGenerator(MATCH_ID_AVAILABLE_CHARACTERS);
    }

    /**
     * Starts a match.
     * Generates a match id and sends it to the client.
     * Also adds the newly created match to the matches map and adds the client to the match.
     */
    @Override
    public void run() {
        // TODO: Change the length of the generated match id according to the number of ongoing matches.
        String potentialMatchID = generator.nextString(MATCH_ID_LENGTH);
        while (controllers.matchExists(potentialMatchID)) {
            potentialMatchID = generator.nextString(MATCH_ID_LENGTH);
        }

        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            MatchController createdMatch = controllers.addMatch(potentialMatchID);
            var addedPlayer = createdMatch.addPlayer(socket);
            outputStream.writeObject(
                    new StartGameReplyMessage(true, potentialMatchID, createdMatch.getMatchFEN(), addedPlayer)
            );
        }
        catch (IOException e) {
            SafePrint.printErr("Unknown error while replying to a start match request");
            SafePrint.printErr(e.getMessage());
        }
    }

    /**
     * Class to generates a random string of a given length.
     */
    private class RandomStringGenerator {
        private final String availableCharacters;
        private final Random random;

        /**
         * Constructor for the RandomStringGenerator.
         * @param availableCharacters The characters to generate the string from.
         */
        public RandomStringGenerator(String availableCharacters) {
            this.availableCharacters = availableCharacters;
            random = new Random();
        }

        /**
         * Generates a random string of a given length.
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
         * @return The generated character.
         */
        private char nextChar() {
            int randomIndex = random.nextInt(availableCharacters.length());
            return availableCharacters.charAt(randomIndex);
        }
    }
}
