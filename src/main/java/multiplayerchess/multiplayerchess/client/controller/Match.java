package multiplayerchess.multiplayerchess.client.controller;

import multiplayerchess.multiplayerchess.client.controller.parsing.FENParser;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.Position;

/**
 * This class represents a match between two players.
 */
public final class Match {
    // public static final String NormalStartingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final Player player;
    private final String matchID;
    private Board board;
    private boolean ourTurn;
    private Position enPassant;
    // TODO: add last move

    /**
     * Constructor for a match.
     *
     * @param startingFEN The FEN string of the board.
     * @param player      Which player we are playing as.
     * @param matchID     The ID of the match.
     */
    public Match(String startingFEN, Player player, String matchID) {
        board = new Board(startingFEN);
        Player playerOnTurn = FENParser.getCurrentPlayer(startingFEN);
        this.ourTurn = player == playerOnTurn;
        this.player = player;
        this.matchID = matchID;
        this.enPassant = FENParser.getEnPassant(startingFEN);
    }

    /**
     * Returns the player who we are playing as.
     *
     * @return The player who we are playing as.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the board of the match.
     *
     * @return The board of the match.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the ID of the match.
     *
     * @return The ID of the match.
     */
    public String getMatchID() {
        return matchID;
    }

    /**
     * Returns whether it is our turn.
     *
     * @return Whether it is our turn.
     */
    public boolean isOurTurn() {
        return ourTurn;
    }

    /**
     * Answers whether the move is a capturing move.
     *
     * @param pieceType The type of the piece that is moving.
     * @param start     The start position of the piece.
     * @param end       The end position of the piece.
     * @return Whether the move is a capturing move.
     */
    public boolean isCapture(PieceType pieceType, Position start, Position end) {
        return board.getPiece(end) != null || (pieceType == PieceType.PAWN && enPassant != null && enPassant.equals(end));
    }

    /**
     * Updates the board to reflect the given FEN string.
     *
     * @param FEN The FEN string to update the board to.
     */
    public void nextTurn(String FEN) {
        ourTurn = !ourTurn;
        enPassant = FENParser.getEnPassant(FEN);
        board = new Board(FEN);
    }
}
