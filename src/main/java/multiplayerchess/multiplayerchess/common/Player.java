package multiplayerchess.multiplayerchess.common;

import java.io.Serializable;

/**
 * An enum representing the color of a player.
 */
public enum Player implements Serializable {
    BLACK,
    WHITE;

    static final long serialVersionUID = 0x98765;

    /**
     * Returns the other type of player.
     * @return The other type of player.
     */
    public Player opposite() {
        return this == Player.WHITE ? Player.BLACK : Player.WHITE;
    }

    /**
     * Returns the color associated with the player.
     * @return The color associated with the player.
     */
    public Color getColor() {
        return this == Player.WHITE ? Color.WHITE : Color.BLACK;
    }
}
