package multiplayerchess.multiplayerchess.server;

import multiplayerchess.multiplayerchess.common.messages.MessageType;
import multiplayerchess.multiplayerchess.common.networking.Networking;
import multiplayerchess.multiplayerchess.server.networking.MatchesMap;
import multiplayerchess.multiplayerchess.server.networking.PlayerConnectionController;
import multiplayerchess.multiplayerchess.server.networking.PreMatchController;

import java.io.IOException;
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
            PlayerConnectionController controller = PlayerConnectionController.createController(socket);

            PreMatchController preMatchController = new PreMatchController(controller, controllers);

            controller.addCallback(MessageType.START_GAME, preMatchController::startMatch);
            controller.addCallback(MessageType.JOIN_GAME, preMatchController::joinMatch);

            controller.start();
        }
        catch (IOException e) {
            SafePrint.printErr("Connection failed");
            SafePrint.printErr(e.getMessage());
        }
    }
}
