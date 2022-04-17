package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.client.controller.parsing.FENParser;
import multiplayerchess.multiplayerchess.client.controller.pieces.Piece;
import multiplayerchess.multiplayerchess.common.Player;

public class Match {
    private static final String StartingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final Piece[][] board;
    private final Player currentPlayer;

    public Match() {
        board = FENParser.ParseBoard(StartingFEN);
        currentPlayer = Player.White;
    }
}
