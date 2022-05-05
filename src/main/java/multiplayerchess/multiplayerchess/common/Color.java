package multiplayerchess.multiplayerchess.common;

/**
 * Enum for the color of a piece.
 */
public enum Color {
    BLACK,
    WHITE;

    /**
     * Get the Player who is associated with this color.
     *
     * @return The Player who is associated with this color.
     */
    public Player getPlayer() {
        return this == Color.WHITE ? Player.WHITE : Player.BLACK;
    }

}
