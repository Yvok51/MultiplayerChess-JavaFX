package multiplayerchess.multiplayerchess.client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Window;

public final class UIPopup extends Popup {
    // private final Popup popup;
    private final Label popupLabel;
    private final Button popupButton;
    private final Window window;

    public UIPopup(Window window) {
        this(window, "", "");
    }

    public UIPopup(Window window, String buttonText) {
        this(window, buttonText, "");
    }

    public UIPopup(Window window, String buttonText, String message) {
        this.window = window;
        popupLabel = new Label(message);
        popupButton = new Button(buttonText);
        this.getContent().add(popupLabel);
        this.getContent().add(popupButton);
    }

    public void setButtonAction(EventHandler<ActionEvent> eventHandler) {
        popupButton.setOnAction(eventHandler);
    }

    public void show(String message) {
        popupLabel.setText(message);
        this.show(window);
    }

    public void show() {
        this.show(window);
    }
}
