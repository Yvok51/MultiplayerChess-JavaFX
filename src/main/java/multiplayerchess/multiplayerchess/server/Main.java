package multiplayerchess.multiplayerchess.server;

import multiplayerchess.multiplayerchess.common.networking.Networking;
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
        SafePrint.print("Starting server...");
        try (ServerSocket serverSocket = new ServerSocket(Networking.SERVER_PORT)) {

            SafePrint.print("Server started");
            SafePrint.print("Listening for connections...");
            serverSocket.setReuseAddress(true);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    SafePrint.print("Connection accepted");
                    dispatchMessage(socket, controllers);
                    // SafePrint.print("Connection dispatched");
                }
                catch (IOException e) {
                    SafePrint.print("Client unexpectedly disconnected");
                    SafePrint.print(e.getMessage());
                }
            }
        }
        catch (IOException e) {
            SafePrint.print("Server unexpectedly crashed");
            SafePrint.print(e.getMessage());
        }
    }

    /**
     * Dispatches the message received from the client to the appropriate handler
     * @param socket the socket from which the message was received
     * @param controllers the map of matches
     */
    public static void dispatchMessage(Socket socket, MatchesMap controllers) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ClientMessage message = (ClientMessage) inputStream.readObject();

            if (message instanceof StartGameMessage) {
                new Thread(new StartMatchThread(socket, controllers)).start();
            } else if (message instanceof JoinMatchMessage joinMatchMessage) {
                new Thread(new JoinMatchThread(socket, controllers, joinMatchMessage)).start();
            } else {
                SafePrint.printErr("Unexpected message"); // maybe start a new thread which sends an error message back
            }
        }
        catch (IOException e) {
            SafePrint.printErr("Connection failed");
            SafePrint.printErr(e.getMessage());
        }
        catch (ClassNotFoundException e) {
            SafePrint.printErr("Input Messages have unknown format");
            SafePrint.printErr(e.getMessage());
        }
    }
}
