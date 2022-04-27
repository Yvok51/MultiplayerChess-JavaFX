package multiplayerchess.multiplayerchess.server;

import multiplayerchess.multiplayerchess.common.Networking;
import multiplayerchess.multiplayerchess.common.messages.ClientMessage;
import multiplayerchess.multiplayerchess.common.messages.JoinMatchMessage;
import multiplayerchess.multiplayerchess.common.messages.StartGameMessage;
import multiplayerchess.multiplayerchess.server.networking.JoinMatchThread;
import multiplayerchess.multiplayerchess.server.networking.MatchesMap;
import multiplayerchess.multiplayerchess.server.networking.StartMatchThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    /**
     * Starts the server. The server listens for incoming connections and dispatches
     * the messages received from the clients.
     * @param args Arguments are ignored
     */
    public static void main(String[] args) {
        MatchesMap controllers = new MatchesMap();
        try (ServerSocket serverSocket = new ServerSocket(Networking.SERVER_PORT)) {

            serverSocket.setReuseAddress(true);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    dispatchMessage(socket, controllers);
                }
                catch (IOException e) {
                    System.out.println("Client unexpectedly disconnected");
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (IOException e) {
            System.out.println("Server unexpectedly crashed");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Dispatches the message received from the client to the appropriate handler
     * @param socket the socket from which the message was received
     * @param controllers the map of matches
     */
    public static void dispatchMessage(Socket socket, MatchesMap controllers) {
        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            ClientMessage message = (ClientMessage) inputStream.readObject();
            if (message instanceof StartGameMessage) {
                new Thread(new StartMatchThread(socket, controllers)).start();
            } else if (message instanceof JoinMatchMessage joinMatchMessage) {
                new Thread(new JoinMatchThread(socket, controllers, joinMatchMessage)).start();
            } else {
                System.err.println("Unexpected message"); // maybe start a new thread which sends an error message back
            }
        }
        catch (IOException e) {
            System.err.println("Connection failed");
            System.err.println(e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.err.println("Input stream has unknown format");
            System.err.println(e.getMessage());
        }
    }
}
