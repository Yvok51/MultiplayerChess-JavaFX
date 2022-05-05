package multiplayerchess.multiplayerchess.server;

import multiplayerchess.multiplayerchess.common.messages.MessageType;
import multiplayerchess.multiplayerchess.common.networking.Networking;
import multiplayerchess.multiplayerchess.server.networking.MatchesMap;
import multiplayerchess.multiplayerchess.server.networking.PlayerConnectionController;
import multiplayerchess.multiplayerchess.server.networking.PreMatchController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class Main {
    /**
     * Starts the server. The server listens for incoming connections and dispatches
     * the messages received from the clients.
     *
     * @param args Arguments are ignored
     */
    public static void main(String[] args) {
        MatchesMap controllers = new MatchesMap();
        SafeLog.log(Level.INFO, "Starting server...");
        try (ServerSocket serverSocket = new ServerSocket(Networking.SERVER_PORT)) {

            SafeLog.log(Level.INFO, "Server started");
            SafeLog.log(Level.INFO, "Listening for connections...");
            serverSocket.setReuseAddress(true);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    SafeLog.log(Level.INFO, "Connection accepted");
                    startConnection(socket, controllers);
                }
                catch (IOException e) {
                    SafeLog.log(Level.WARNING, "Client unexpectedly disconnected: " + e.getMessage());
                }
            }
        }
        catch (IOException e) {
            SafeLog.log(Level.SEVERE, "Server unexpectedly crashed: " + e.getMessage());
        }
    }

    /**
     * Starts a listener and writer for a client and sets up starting callbacks.
     *
     * @param socket      the socket of the connection
     * @param controllers the map of match controllers
     */
    public static void startConnection(Socket socket, MatchesMap controllers) {
        try {
            PlayerConnectionController controller = PlayerConnectionController.createController(socket);

            PreMatchController preMatchController = new PreMatchController(controller, controllers);

            controller.addCallback(MessageType.START_GAME, preMatchController::startMatch);
            controller.addCallback(MessageType.JOIN_GAME, preMatchController::joinMatch);

            controller.start();
        }
        catch (IOException e) {
            SafeLog.log(Level.WARNING, "Connection failed: " + e.getMessage());
        }
    }
}
