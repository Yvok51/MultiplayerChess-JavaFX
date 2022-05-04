package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.messages.JoinMatchMessage;
import multiplayerchess.multiplayerchess.common.messages.JoinMatchReplyMessage;
import multiplayerchess.multiplayerchess.server.SafePrint;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

/**
 * Thread that handles the joining of a client to a match.
 */
public class JoinMatchThread implements Runnable {
    private final Socket socket;
    private final MatchesMap controllers;
    private final JoinMatchMessage message;

    /**
     * JoinMatchThread constructor.
     * @param socket The socket the client that wants to join the match is connected to.
     * @param controllers The map of match controllers.
     * @param message The message that contains the match ID.
     */
    public JoinMatchThread(Socket socket, MatchesMap controllers, JoinMatchMessage message) {
        this.socket = socket;
        this.controllers = controllers;
        this.message = message;
    }

    /**
     * Looks whether the match ID is valid and if so, adds the client to the match.
     * Replies to the client with a JoinMatchReplyMessage.
     */
    @Override
    public void run() {
        Optional<MatchController> matchToJoin = controllers.getMatch(message.matchID);
        if (matchToJoin.isEmpty() || !matchToJoin.get().hasOpenSpot()) {
            sendRejection();
            return;
        }

        var matchController = matchToJoin.get();
        Player joinedAs;
        try {
            joinedAs = matchController.addPlayer(socket);
        }
        catch (IOException e) {
            SafePrint.printErr("Error while adding a player to a match: " + e.getMessage());
            sendRejection();
            return;
        }

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new JoinMatchReplyMessage(
                    true, matchController.getMatchFEN(), joinedAs, matchController.getMatchID()
                )
            );
        }
        catch (IOException e) {
            SafePrint.printErr("Unknown error while replying to a Join Match message: " + e.getMessage());
        }
    }

    private void sendRejection() {
        // close the socket as no more messages will be exchanged
        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            outputStream.writeObject(new JoinMatchReplyMessage(false, "", null, message.matchID));
        }
        catch (IOException e) {
            SafePrint.printErr("Unknown error while replying to a Join Match message: " + e.getMessage());
        }
    }
}
