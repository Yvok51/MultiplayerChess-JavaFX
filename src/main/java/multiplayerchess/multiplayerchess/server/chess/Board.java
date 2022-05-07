package multiplayerchess.multiplayerchess.server.chess;

import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.chess.parsing.FENParser;
import multiplayerchess.multiplayerchess.server.chess.pieces.Piece;

/**
 * Representation of the chess board, represented by a 2D array of pieces.
 */
public final class Board {

    public static final int MinBoardRow = 0;
    public static final int MaxBoardRow = 7;
    public static final int MinBoardColumn = 0;
    public static final int MaxBoardColumn = 7;
    private final Piece[][] board;

    /**
     * The Board constructor
     *
     * @param startingFEN The FEN to parse the board from
     */
    public Board(String startingFEN) {
        board = FENParser.ParseBoard(startingFEN);
    }

    /**
     * Gets the piece on the position
     *
     * @param position The position to get the piece from
     * @return The piece on the position
     */
    public Piece getPiece(Position position) {
        return board[position.row][position.column];
    }

    /**
     * Gets the piece on the position
     *
     * @param row    The row of the position
     * @param column The column of the position
     * @return The piece on the position
     */
    public Piece getPiece(int row, int column) {
        return board[row][column];
    }

    /**
     * Sets the piece on the given position
     *
     * @param position Position to put the piece on
     * @param piece    The piece to put down
     */
    public void setPiece(Position position, Piece piece) {
        board[position.row][position.column] = piece;
    }

    /**
     * Clears any piece that is on the given position
     *
     * @param position The position to clear
     */
    public void clearPiece(Position position) {
        board[position.row][position.column] = null;
    }
}
