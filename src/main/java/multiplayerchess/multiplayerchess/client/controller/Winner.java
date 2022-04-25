package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.common.Player;

public enum Winner {
    WHITE, BLACK, NONE;

    public static Winner getWinnerFromPlayer(Player player) {
        if (player == null) {
            return Winner.NONE;
        }
        return player == Player.White ? Winner.WHITE : Winner.BLACK;
    }
}
