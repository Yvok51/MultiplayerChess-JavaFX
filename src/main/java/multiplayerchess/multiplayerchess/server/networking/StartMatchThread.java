package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.messages.StartGameReplyMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class StartMatchThread implements Runnable {
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERALS = "0123456789";
    private static final String MATCH_ID_AVAILABLE_CHARACTERS = LOWER_CASE + UPPER_CASE + NUMERALS;
    private static final int MATCH_ID_LENGTH = 8;
    private final Socket socket;
    MatchesMap controllers;
    RandomStringGenerator generator;

    public StartMatchThread(Socket socket, MatchesMap controllers) {
        this.socket = socket;
        this.controllers = controllers;
        this.generator = new RandomStringGenerator(MATCH_ID_AVAILABLE_CHARACTERS);
    }

    @Override
    public void run() {
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
            System.err.println("Unknown error while replying to a start match request");
            System.err.println(e.getMessage());
        }
    }

    private class RandomStringGenerator {
        private final String availableCharacters;
        private final Random random;

        public RandomStringGenerator(String availableCharacters) {
            this.availableCharacters = availableCharacters;
            random = new Random();
        }

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

        private char nextChar() {
            int randomIndex = random.nextInt(availableCharacters.length());
            return availableCharacters.charAt(randomIndex);
        }
    }
}
