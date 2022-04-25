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
    private Socket blackPLayerSocket;
    private boolean gameOngoing;

    public MatchController(String matchID, MatchesMap controllers) {
        match = new Match();
        whitePlayerSocket = null;
        blackPLayerSocket = null;
        bothPlayersPresentLatch = new CountDownLatch(1);
        this.matchID = matchID;
        gameOngoing = true;
        this.controllers = controllers;
    }

    private static Move createMoveFromTurnMessage(TurnMessage message) {
        return new Move(message.pieceType, message.startingPosition, message.endingPosition, message.isCapture);
    }

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
            var whitesMessage = acceptPlayerMessage(whitePlayerSocket);
            if (whitesMessage.isEmpty()) {
                try {
                    playerDisconnected(Player.WHITE);
                }
                catch (IOException ignored) {
                }
                break;
            }

            handlePlayerMessage(whitesMessage.get(), Player.WHITE);

            var blacksMessage = acceptPlayerMessage(blackPLayerSocket);
            if (blacksMessage.isEmpty()) {
                try {
                    playerDisconnected(Player.BLACK);
                }
                catch (IOException ignored) {
                }
                break;
            }

            handlePlayerMessage(blacksMessage.get(), Player.BLACK);
        }

        try {
            if (!whitePlayerSocket.isClosed())
                whitePlayerSocket.close();
            if (!blackPLayerSocket.isClosed())
                blackPLayerSocket.close();
        }
        catch (IOException ignored) {
        }

        controllers.matchEnded(matchID);
    }

    public boolean hasOpenSpot() {
        return whitePlayerSocket == null || blackPLayerSocket == null;
    }

    public Player addPlayer(Socket player) {
        if (whitePlayerSocket == null) {
            whitePlayerSocket = player;
            return Player.WHITE;
        } else if (blackPLayerSocket == null) {
            blackPLayerSocket = player;
            // When both players are present -> wake up the thread waiting in the run method
            bothPlayersPresentLatch.countDown();
            return Player.BLACK;
        }

        return null; // No open spot - should never happen
    }

    public String getMatchFEN() {
        return match.getFEN();
    }

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

    private TurnReplyMessage handleTurnMessage(TurnMessage message) {
        Move move = createMoveFromTurnMessage(message);
        boolean success = match.makeMove(move);
        boolean gameOver = match.gameOver();

        if (gameOver) {
            var winner = match.winner();
            return winner.map(player -> new TurnReplyMessage(success, match.getFEN(), gameOver, player, message.matchID))
                    .orElseGet(() -> new TurnReplyMessage(success, match.getFEN(), gameOver, null, message.matchID));

        } else {
            return new TurnReplyMessage(success, match.getFEN(), gameOver, null, message.matchID);
        }
    }

    private Socket playerToSocket(Player player) {
        return player == Player.WHITE ? whitePlayerSocket : blackPLayerSocket;
    }

    private Optional<ClientOngoingMatchMessage> acceptPlayerMessage(Socket playerSocket) {
        try (ObjectInputStream input = new ObjectInputStream(playerSocket.getInputStream())) {
            var message = (ClientOngoingMatchMessage) input.readObject();
            return Optional.of(message);
        }
        catch (IOException | ClassNotFoundException ignored) {
            // returning a null message will signify there was an error with the socket -> client disconnected
            // The client won't send anything but an OngoingMatchMessage
            return Optional.empty();
        }
    }

    private void playerDisconnected(Player disconnectedPlayer) throws IOException {
        gameOngoing = false;
        sendOpponentDisconnectedMessage(disconnectedPlayer);
    }

    private void opponentResigned(Player resignedPlayer) throws IOException {
        gameOngoing = false;
        sendOpponentResignedMessage(resignedPlayer);
    }

    private void sendOpponentDisconnectedMessage(Player disconnectedPlayer) throws IOException {
        OpponentDisconnectedMessage message = new OpponentDisconnectedMessage(matchID);
        if (disconnectedPlayer == Player.WHITE) {
            sendMessage(blackPLayerSocket, message);
        } else if (disconnectedPlayer == Player.BLACK) {
            sendMessage(whitePlayerSocket, message);
        }
    }

    private void sendOpponentResignedMessage(Player resignedPlayer) throws IOException {
        OpponentResignedMessage message = new OpponentResignedMessage(matchID);
        if (resignedPlayer == Player.WHITE) {
            sendMessage(blackPLayerSocket, message);
        } else if (resignedPlayer == Player.BLACK) {
            sendMessage(whitePlayerSocket, message);
        }
    }

    /**
     * Sends a message to the both sockets
     *
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
            sendMessage(blackPLayerSocket, message);
        }
        catch (IOException ignored) {
            return Optional.of(Player.BLACK);
        }
        return Optional.empty();
    }

    private void sendMessage(Socket socket, Message message) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
            output.writeObject(message);
        }
    }
}
