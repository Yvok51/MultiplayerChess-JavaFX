package multiplayerchess.multiplayerchess.client.ui.mainmenu;

import javafx.event.ActionEvent;

public class MainMenuController {
    public void onStartGame(ActionEvent e) {
        System.out.println("Start game");
    }

    public void onJoinGame(ActionEvent e) {
        System.out.println("Join game");
    }

    public void onQuit(ActionEvent e) {
        System.out.println("Quit");
    }
}
