package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.common.Player;

/**
 * This class is used to determine the winner of the game.
 */
public enum Winner {
    WHITE,
    BLACK,
    NONE;

    /**
     * Translate from the player enum to the winner enum.
     *
     * @param player The player.
     * @return The winner.
     */
    public static Winner getWinnerFromPlayer(Player player) {
        if (player == null) {
            return Winner.NONE;
        }
        return player == Player.WHITE ? Winner.WHITE : Winner.BLACK;
    }

    @Override
    public String toString() {
        return switch (this) {
            case WHITE -> "White";
            case BLACK -> "Black";
            case NONE -> "Draw";
        };
    }
}
