package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.messages.*;
import multiplayerchess.multiplayerchess.server.SafeLog;
import multiplayerchess.multiplayerchess.server.chess.Match;
import multiplayerchess.multiplayerchess.server.chess.Move;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

/**
 * The MatchController is the main class handling the match.
 * It is responsible for handling the messages sent by the clients and the game logic.
 */
public final class MatchController extends Thread {
    private final Match match;
    private final CountDownLatch bothPlayersPresentLatch;
    private final String matchID;
    private final MatchesMap controllers;
    private final AtomicBoolean gameOngoing;
    private final AtomicBoolean gameStarted;
    private PlayerConnectionController whitePlayerController;
    private PlayerConnectionController blackPlayerController;

    /**
     * The MatchController constructor
     *
     * @param matchID     The match ID
     * @param controllers The MatchesMap to which this MatchController belongs
     */
    public MatchController(String matchID, MatchesMap controllers) {
        match = new Match();
        whitePlayerController = null;
        blackPlayerController = null;
        bothPlayersPresentLatch = new CountDownLatch(1);
        this.matchID = matchID;
        gameOngoing = new AtomicBoolean(true);
        gameStarted = new AtomicBoolean(false);
        this.controllers = controllers;
    }

    /**
     * Gets a {@link multiplayerchess.multiplayerchess.server.chess.Move} representing the move to be performed
     * according to the {@link TurnMessage}
     *
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
        while (!gameStarted.get()) {
            try {
                bothPlayersPresentLatch.await();
            }
            catch (InterruptedException ignored) {
            }
        }

        final long tenSeconds = 10_000;
        while (gameOngoing.get()) {
            broadcastMessage(new HeartbeatMessage(matchID));

            try {
                Thread.sleep(tenSeconds);
            }
            catch (InterruptedException ignored) {
            }

            if (!gameOngoing.get()) {
                return;
            }

            if (!blackPlayerController.isRunning() || !blackPlayerController.hasHeartbeatOccurred()) {
                playerDisconnected(Player.BLACK);
            }
            if (!whitePlayerController.isRunning() || !whitePlayerController.hasHeartbeatOccurred()) {
                playerDisconnected(Player.WHITE);
            }

            whitePlayerController.clearHeartbeat();
            blackPlayerController.clearHeartbeat();
        }
    }

    /**
     * Answers whether there is still room for a new player to join
     *
     * @return true if there is room for a new player to join, false otherwise
     */
    public boolean hasOpenSpot() {
        return whitePlayerController == null || blackPlayerController == null;
    }

    /**
     * Add a player to the match
     *
     * @param playerController The controller of the connection of the player to add
     * @return The player color of the player added
     */
    public Player addPlayer(PlayerConnectionController playerController) {
        if (whitePlayerController == null) {
            whitePlayerController = playerController;
            whitePlayerController.addCallback(MessageType.RESIGNED, this::playerResignedHandler);
            whitePlayerController.addCallback(MessageType.TURN, this::playerTurnHandler);
            whitePlayerController.addCallback(MessageType.DISCONNECTED, this::playerDisconnectedHandler);

            return Player.WHITE;
        } else if (blackPlayerController == null) {
            blackPlayerController = playerController;
            blackPlayerController.addCallback(MessageType.RESIGNED, this::playerResignedHandler);
            blackPlayerController.addCallback(MessageType.TURN, this::playerTurnHandler);
            blackPlayerController.addCallback(MessageType.JOIN_GAME, this::joinedPlayerHasAcknowledgedConnectionHandler);
            blackPlayerController.addCallback(MessageType.DISCONNECTED, this::playerDisconnectedHandler);

            return Player.BLACK;
        }

        return null; // No open spot - should never happen
    }

    /**
     * Gets the FEN representation of the current match situation
     *
     * @return The FEN representation
     */
    public String getMatchFEN() {
        return match.getFEN();
    }

    /**
     * Gets the match ID of this match
     *
     * @return the match ID
     */
    public String getMatchID() {
        return matchID;
    }

    /**
     * Handles a turn message sent by a player
     *
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
        } else {
            sendMessage(reply, player);
        }
    }

    /**
     * Handles an acknowledgement message sent by a player that he has connected.
     * This is used to synchronize the server with both players
     *
     * @param message The acknowledgement message to handle
     */
    private void joinedPlayerHasAcknowledgedConnectionHandler(Message message) {
        broadcastMessage(new OpponentConnectedMessage());
        blackPlayerController.removeCallback(MessageType.JOIN_GAME, this::joinedPlayerHasAcknowledgedConnectionHandler);

        // When both players are present -> wake up the thread waiting in the run method and start sending heartbeat messages
        gameStarted.set(true);
        bothPlayersPresentLatch.countDown();
    }

    /**
     * Handles when a player resigns
     *
     * @param message The message containing the player's resignation
     */
    private void playerResignedHandler(Message message) {
        var resignMessage = (ResignMessage) message;
        sendMessage(new OpponentResignedMessage(matchID), resignMessage.player.opposite());
        endGame();
    }

    /**
     * Handles when a player disconnects
     *
     * @param message The message containing the player who disconnected
     */
    private void playerDisconnectedHandler(Message message) {
        var disconnectMessage = (DisconnectMessage) message;
        sendMessage(new OpponentDisconnectedMessage(matchID), disconnectMessage.disconnectingPlayer.opposite());
        endGame();
    }

    /**
     * Handles a situation when a player disconnects
     *
     * @param player The player who disconnected
     */
    private void playerDisconnected(Player player) {
        sendMessage(new OpponentDisconnectedMessage(matchID), player.opposite());
        endGame();
    }

    /**
     * Handles a turn message from a player.
     * Translate the message into a Move and performs it on the match
     *
     * @param message The message to handle
     * @return The reply to send to the player
     */
    private TurnReplyMessage handleTurnMessage(TurnMessage message) {
        Move move = createMoveFromTurnMessage(message);
        boolean success = match.makeMove(move);

        SafeLog.log(Level.INFO, "FEN After move attempt: " + match.getFEN());

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
     * Ends the game, gets rid of all resources
     */
    private void endGame() {
        try {
            if (whitePlayerController != null) {
                whitePlayerController.close();
            }
        }
        catch (IOException ignored) {
        }
        try {
            if (blackPlayerController != null) {
                blackPlayerController.close();
            }
        }
        catch (IOException ignored) {
        }

        gameStarted.set(true);
        gameOngoing.set(false);
        controllers.matchEnded(matchID);
        this.interrupt();
    }

    /**
     * Sends a message to the both players
     *
     * @param message The message to send
     */
    private void broadcastMessage(ServerMessage message) {
        sendMessage(message, Player.WHITE);
        sendMessage(message, Player.BLACK);
    }

    /**
     * Sends a message to one of the players. If the player is not present, the message is discarded
     *
     * @param message The message to send
     * @param player  The player to send the message to
     */
    private void sendMessage(ServerMessage message, Player player) {
        if (player == Player.WHITE && whitePlayerController != null) {
            whitePlayerController.sendMessage(message);
        } else if (player == Player.BLACK && blackPlayerController != null) {
            blackPlayerController.sendMessage(message);
        }
    }
}
