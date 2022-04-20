package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.client.controller.parsing.FENParser;
import multiplayerchess.multiplayerchess.client.controller.pieces.Piece;
import multiplayerchess.multiplayerchess.common.Player;

public class Match {
    private static final String NormalStartingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final Board board;
    private boolean ourTurn;
    private final Player player;
    private final String matchID;

    public Match(String startingFEN, Player player, String matchID) {
        board = new Board(startingFEN);
        Player playerOnTurn = FENParser.getCurrentPlayer(startingFEN);
        this.ourTurn = player == playerOnTurn;
        this.player = player;
        this.matchID = matchID;
    }


}
