package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.messages.*;
import multiplayerchess.multiplayerchess.server.controller.Match;
import multiplayerchess.multiplayerchess.server.controller.Move;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public final class MatchController extends Thread {
    private final Match match;
    private final CountDownLatch bothPlayersPresentLatch;
    private final String matchID;
    private final MatchesMap controllers;
    private PlayerConnectionController whitePlayerController;
    private PlayerConnectionController blackPlayerController;
    private boolean gameOngoing;

    /**
     * The MatchController constructor
     * @param matchID The match ID
     * @param controllers The MatchesMap to which this MatchController belongs
     */
    public MatchController(String matchID, MatchesMap controllers) {
        match = new Match();
        whitePlayerController = null;
        blackPlayerController = null;
        bothPlayersPresentLatch = new CountDownLatch(1);
        this.matchID = matchID;
        gameOngoing = true;
        this.controllers = controllers;

    }

    /**
     * Get a Move representing the move to be performed according to the TurnMessage
     * @param message The TurnMessage on which to base the move
     * @return The move to be performed
     */
    private static Move createMoveFromTurnMessage(TurnMessage message) {
        return new Move(message.pieceType, message.startingPosition, message.endingPosition, message.isCapture);
    }

    /**
     * Runs until the end of the match always waiting for a message from both players
     * First waits until both players are present, then handles messages from both players until the match ends
     * At the end of the match sends the result to the players, closes the sockets
     * and disposes itself from the MatchesMap.
     */
    @Override
    public void run() {
        while (hasOpenSpot()) {
            try {
                bothPlayersPresentLatch.await();
            }
            catch (InterruptedException ignored) {
            }
        }

        broadcastMessage(new OpponentConnectedMessage());

        final long fiveSeconds = 5000;
        while (gameOngoing) {
            broadcastMessage(new HeartbeatMessage(matchID));

            try {
                Thread.sleep(fiveSeconds);
            }
            catch (InterruptedException ignored) {
            }

            if (!whitePlayerController.isRunning() || !whitePlayerController.hasHeartbeatOccurred()) {
                playerDisconnected(Player.WHITE);
            }
            if (!blackPlayerController.isRunning() || !blackPlayerController.hasHeartbeatOccurred()) {
                playerDisconnected(Player.BLACK);
            }

            whitePlayerController.clearHeartbeat();
            blackPlayerController.clearHeartbeat();
        }
    }

    /**
     * Answers whether there is still room for a new player to join
     * @return true if there is room for a new player to join, false otherwise
     */
    public boolean hasOpenSpot() {
        return whitePlayerController == null || blackPlayerController == null;
    }

    /**
     * Add a player to the match
     * @param playerSocket The socket of the player to add
     * @return The player color of the player added
     */
    public Player addPlayer(Socket playerSocket) throws IOException {
        if (whitePlayerController == null) {
            whitePlayerController = PlayerConnectionController.createController(playerSocket);
            whitePlayerController.addCallback(MessageType.RESIGNED, this::playerResignedHandler);
            whitePlayerController.addCallback(MessageType.TURN, this::playerTurnHandler);
            whitePlayerController.start();

            return Player.WHITE;
        }
        else if (blackPlayerController == null) {
            blackPlayerController = PlayerConnectionController.createController(playerSocket);
            blackPlayerController.addCallback(MessageType.RESIGNED, this::playerResignedHandler);
            blackPlayerController.addCallback(MessageType.TURN, this::playerTurnHandler);
            blackPlayerController.start();

            // When both players are present -> wake up the thread waiting in the run method
            bothPlayersPresentLatch.countDown();
            return Player.BLACK;
        }

        return null; // No open spot - should never happen
    }

    /**
     * Get the FEN representation of the current match situation
     * @return The FEN representation
     */
    public String getMatchFEN() {
        return match.getFEN();
    }

    /**
     * Get the match ID of this match
     * @return the match ID
     */
    public String getMatchID() { return matchID; }

    /**
     * Handle a turn message sent by a player
     * @param message The turn message to handle
     */
    private void playerTurnHandler(Message message) {
        var turnMessage = (TurnMessage) message;
        var player = turnMessage.playerColor.getPlayer();
        if (!player.equals(match.getCurrentPlayer())) { // Ignore if it's not the player's turn
            return;
        }

        var reply = handleTurnMessage(turnMessage);
        if (reply.success) {
            broadcastMessage(reply);
            if (reply.gameOver) {
                endGame();
            }
        }
        else {
            sendMessage(reply, player);
        }

    }

    /**
     * Handles when a player resigns
     * @param message The message containing the player's resignation
     */
    private void playerResignedHandler(Message message) {
        var resignMessage = (ResignMessage) message;
        sendMessage(new OpponentResignedMessage(matchID), resignMessage.player.opposite());
        endGame();
    }

    /**
     * Handle when a player disconnects
     * @param player The player who disconnected
     */
    private void playerDisconnected(Player player) {
        sendMessage(new OpponentDisconnectedMessage(matchID), player.opposite());
        endGame();
    }

    /**
     * Handles a turn message from a player.
     * Translate the message into a Move and performs it on the match
     * @param message The message to handle
     * @return The reply to send to the player
     */
    private TurnReplyMessage handleTurnMessage(TurnMessage message) {
        Move move = createMoveFromTurnMessage(message);
        boolean success = match.makeMove(move);
        if (!success) {
            return new TurnReplyMessage(false, match.getFEN(), false, null, message.matchID);
        }

        boolean gameOver = match.gameOver();
        if (gameOver) {
            var winner = match.winner();
            return winner.map(player -> new TurnReplyMessage(success, match.getFEN(), gameOver, player, message.matchID))
                    .orElseGet(() -> new TurnReplyMessage(success, match.getFEN(), gameOver, null, message.matchID));

        }

        return new TurnReplyMessage(success, match.getFEN(), gameOver, null, message.matchID);
    }

    /**
     * End the game, get rid of resources
     */
    private void endGame() {
        try {
            whitePlayerController.close();
        } catch (IOException ignored) {
        }
        try {
            blackPlayerController.close();
        } catch (IOException ignored) {
        }

        controllers.matchEnded(matchID);
        gameOngoing = false;
    }

    /**
     * Sends a message to the both players
     * @param message The message to send
     */
    private void broadcastMessage(ServerMessage message) {
        whitePlayerController.sendMessage(message);
        blackPlayerController.sendMessage(message);
    }

    /**
     * Send a message to one player
     * @param message The message to send
     * @param player The player to send the message to
     */
    private void sendMessage(ServerMessage message, Player player) {
        if (player == Player.WHITE) {
            whitePlayerController.sendMessage(message);
        } else if (player == Player.BLACK) {
            blackPlayerController.sendMessage(message);
        }
    }
}
