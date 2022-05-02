package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.messages.*;
import multiplayerchess.multiplayerchess.server.controller.Match;
import multiplayerchess.multiplayerchess.server.controller.Move;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public final class MatchController extends Thread {
    private final Match match;
    private final CountDownLatch bothPlayersPresentLatch;
    private final String matchID;
    private final MatchesMap controllers;
    private Socket whitePlayerSocket;
    private Socket blackPlayerSocket;
    private boolean gameOngoing;

    /**
     * The MatchController constructor
     * @param matchID The match ID
     * @param controllers The MatchesMap to which this MatchController belongs
     */
    public MatchController(String matchID, MatchesMap controllers) {
        match = new Match();
        whitePlayerSocket = null;
        blackPlayerSocket = null;
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

        // Both players are present
        while (gameOngoing) {
            Socket socket = getCurrentPlayerSocket();
            Player currentPlayer = match.getCurrentPlayer();

            var message = acceptPlayerMessage(socket);
            if (message.isEmpty()) {
                try {
                    playerDisconnected(currentPlayer);
                }
                catch (IOException ignored) {
                }
                break;
            }

            handlePlayerMessage(message.get(), currentPlayer);
        }

        try {
            if (!whitePlayerSocket.isClosed())
                whitePlayerSocket.close();
            if (!blackPlayerSocket.isClosed())
                blackPlayerSocket.close();
        }
        catch (IOException ignored) {
        }

        controllers.matchEnded(matchID);
    }

    /**
     * Answers whether there is still room for a new player to join
     * @return true if there is room for a new player to join, false otherwise
     */
    public boolean hasOpenSpot() {
        return whitePlayerSocket == null || blackPlayerSocket == null;
    }

    /**
     * Add a player to the match
     * @param player The socket of the player to add
     * @return The player color of the player added
     */
    public Player addPlayer(Socket player) {
        if (whitePlayerSocket == null) {
            whitePlayerSocket = player;
            return Player.WHITE;
        } else if (blackPlayerSocket == null) {
            blackPlayerSocket = player;
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

    private Socket getCurrentPlayerSocket() {
        return match.getCurrentPlayer() == Player.WHITE ? whitePlayerSocket : blackPlayerSocket;
    }

    /**
     * Handle the message received from a player.
     * The message is either a TurnMessage or a ResignMessage
     * @param message The message received
     * @param player The player who sent the message
     */
    private void handlePlayerMessage(ClientOngoingMatchMessage message, Player player) {
        if (message instanceof ResignMessage) {
            try {
                opponentResigned(player);
            }
            catch (IOException ignored) {
                // Since one player resigned and the second one disconnected,
                // we don't need to send anything extra and silently ignore the error
            }
        } else if (message instanceof TurnMessage turnMessage) {
            var reply = handleTurnMessage(turnMessage);
            if (reply.success) {
                var disconnectedPlayer = broadcastMessage(reply);
                if (disconnectedPlayer.isPresent()) {
                    try {
                        playerDisconnected(disconnectedPlayer.get());
                    }
                    catch (IOException ignored) {
                        // Both players disconnected -> ignore
                    }
                }
                if (reply.gameOver) {
                    gameOngoing = false;
                }
            } else {
                try {
                    sendMessage(playerToSocket(player), reply);
                }
                catch (IOException ignored) {
                    // The current player disconnected
                    try { // TODO: refactor, 'cause this is ugly
                        playerDisconnected(player);
                    }
                    catch (IOException ignored2) {
                    }
                }
            }
        }

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
     * Return the socket associated with the given player
     * @param player The player whose socket we want
     * @return The socket associated with the given player
     */
    private Socket playerToSocket(Player player) {
        return player == Player.WHITE ? whitePlayerSocket : blackPlayerSocket;
    }

    /**
     * Accepts a message from the given player (socket)
     * @param playerSocket The socket from which the message is received
     * @return The message received. If no message could be received, returns empty
     */
    private Optional<ClientOngoingMatchMessage> acceptPlayerMessage(Socket playerSocket) {
        try {
            ObjectInputStream input = new ObjectInputStream(playerSocket.getInputStream());
            var message = (ClientOngoingMatchMessage) input.readObject();
            return Optional.of(message);
        }
        catch (IOException | ClassNotFoundException ignored) {
            // returning a null message will signify there was an error with the socket -> client disconnected
            // The client won't send anything but an OngoingMatchMessage
            // Socket will be closed by the caller
            return Optional.empty();
        }
    }

    /**
     * Handles the case where a player disconnected.
     * Signifies that the game is over with the gameOngoing flag no matter what.
     * @param disconnectedPlayer The player who disconnected
     * @throws IOException If anything happened to connection of the opponent of the disconnected player
     * i.e. the opponent disconnected
     */
    private void playerDisconnected(Player disconnectedPlayer) throws IOException {
        gameOngoing = false;
        sendOpponentDisconnectedMessage(disconnectedPlayer);
    }

    /**
     * Handles the case where a player resigned.
     * Signifies that the game is over with the gameOngoing flag no matter what.
     * @param resignedPlayer The player who resigned
     * @throws IOException If anything happened to connection of the opponent of the resigned player
     * i.e. the opponent disconnected
     */
    private void opponentResigned(Player resignedPlayer) throws IOException {
        gameOngoing = false;
        sendOpponentResignedMessage(resignedPlayer);
    }

    /**
     * Sends a message to the player that the opponent disconnected
     * @param disconnectedPlayer The player that disconnected
     * @throws IOException If the message could not be sent
     */
    private void sendOpponentDisconnectedMessage(Player disconnectedPlayer) throws IOException {
        OpponentDisconnectedMessage message = new OpponentDisconnectedMessage(matchID);
        if (disconnectedPlayer == Player.WHITE) {
            sendMessage(blackPlayerSocket, message);
        } else if (disconnectedPlayer == Player.BLACK) {
            sendMessage(whitePlayerSocket, message);
        }
    }

    /**
     * Sends a message to the player that the opponent has resigned
     * @param resignedPlayer the player that resigned
     * @throws IOException if anything unexpected happened i.e. the opponent disconnected
     */
    private void sendOpponentResignedMessage(Player resignedPlayer) throws IOException {
        OpponentResignedMessage message = new OpponentResignedMessage(matchID);
        if (resignedPlayer == Player.WHITE) {
            sendMessage(blackPlayerSocket, message);
        } else if (resignedPlayer == Player.BLACK) {
            sendMessage(whitePlayerSocket, message);
        }
    }

    /**
     * Sends a message to the both sockets
     * @param message The message to send
     * @return Failed socket
     */
    private Optional<Player> broadcastMessage(Message message) {
        try {
            sendMessage(whitePlayerSocket, message);
        }
        catch (IOException ignored) {
            return Optional.of(Player.WHITE);
        }
        try {
            sendMessage(blackPlayerSocket, message);
        }
        catch (IOException ignored) {
            return Optional.of(Player.BLACK);
        }
        return Optional.empty();
    }

    /**
     * Sends a message to a socket
     * @param socket The socket to send the message to
     * @param message The message to send
     * @throws IOException If anything happened while sending the message e.g. the socket is closed
     */
    private void sendMessage(Socket socket, Message message) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(message);
    }
}
