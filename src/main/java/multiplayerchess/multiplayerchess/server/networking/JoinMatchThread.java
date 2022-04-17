package multiplayerchess.multiplayerchess.server.networking;

import multiplayerchess.multiplayerchess.common.messages.JoinMatchMessage;
import multiplayerchess.multiplayerchess.common.messages.JoinMatchReplyMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class JoinMatchThread implements Runnable {
    private final Socket socket;
    private final MatchesMap controllers;
    private final JoinMatchMessage message;

    public JoinMatchThread(Socket socket, MatchesMap controllers, JoinMatchMessage message) {
        this.socket = socket;
        this.controllers = controllers;
        this.message = message;
    }

    @Override
    public void run() {
        Optional<MatchController> matchToJoin = controllers.getMatch(message.matchID);
        if (matchToJoin.isEmpty()) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
                outputStream.writeObject(new JoinMatchReplyMessage(false, "", null, message.matchID));
            }
            catch (IOException e) {
                System.err.println("Unknown error while replying to a Join Match message");
                System.err.println(e.getMessage());
            }
            return;
        }

        matchToJoin.get().addPlayer(socket);
    }
}
