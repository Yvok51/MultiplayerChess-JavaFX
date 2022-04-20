package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.client.controller.parsing.FENParser;
import multiplayerchess.multiplayerchess.client.controller.pieces.Piece;
import multiplayerchess.multiplayerchess.common.Player;

public class Match {
    private static final String NormalStartingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final Board board;
    private final Player currentPlayer;
    private boolean ourTurn;
    private Player player;

    public Match(String startignFEN, Player currentPlayer, boolean ourTurn, Player player) {
        board = new Board(startignFEN);
        this.currentPlayer = currentPlayer;
        this.ourTurn = ourTurn;
        this.player = player;
    }
}
