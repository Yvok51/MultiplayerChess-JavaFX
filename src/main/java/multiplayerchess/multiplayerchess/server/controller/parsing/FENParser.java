package multiplayerchess.multiplayerchess.server.controller.parsing;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.Board;
import multiplayerchess.multiplayerchess.server.controller.pieces.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Parser for FEN notation into a memory representation of a chess game.
 */
public class FENParser {
    private static final Map<PieceType, Function<Color, Character>> pieceToCharTranslation;
    private static final Map<Character, Function<Position, Piece>> charToPieceTranslation;

    static {
        Map<PieceType, Function<Color, Character>> builder = new HashMap<>();
        builder.put(PieceType.KING, (color) -> color == Color.WHITE ? 'K' : 'k');
        builder.put(PieceType.QUEEN, (color) -> color == Color.WHITE ? 'Q' : 'q');
        builder.put(PieceType.BISHOP, (color) -> color == Color.WHITE ? 'B' : 'b');
        builder.put(PieceType.KNIGHT, (color) -> color == Color.WHITE ? 'N' : 'n');
        builder.put(PieceType.ROOK, (color) -> color == Color.WHITE ? 'R' : 'r');
        builder.put(PieceType.PAWN, (color) -> color == Color.WHITE ? 'P' : 'p');

        pieceToCharTranslation = Collections.unmodifiableMap(builder);

        Map<Character, Function<Position, Piece>> pieceBuilder = new HashMap<>();
        pieceBuilder.put('k', (position) -> new King(Color.BLACK));
        pieceBuilder.put('K', (position) -> new King(Color.WHITE));
        pieceBuilder.put('p', (position) -> position.row == 6
                ? new Pawn(Color.BLACK, false)
                : new Pawn(Color.BLACK, true));
        pieceBuilder.put('P', (position) -> position.row == 1
                ? new Pawn(Color.WHITE, false)
                : new Pawn(Color.WHITE, true));
        pieceBuilder.put('n', (position) -> new Knight(Color.BLACK));
        pieceBuilder.put('N', (position) -> new Knight(Color.WHITE));
        pieceBuilder.put('b', (position) -> new Bishop(Color.BLACK));
        pieceBuilder.put('B', (position) -> new Bishop(Color.WHITE));
        pieceBuilder.put('r', (position) -> new Rook(Color.BLACK));
        pieceBuilder.put('R', (position) -> new Rook(Color.WHITE));
        pieceBuilder.put('q', (position) -> new Queen(Color.BLACK));
        pieceBuilder.put('Q', (position) -> new Queen(Color.WHITE));

        charToPieceTranslation = Collections.unmodifiableMap(pieceBuilder);
    }

    /**
     * Parses the FEN string into a board.
     * @param FEN The FEN string to parse and create a board from.
     * @return The board created from the FEN string.
     */
    public static Piece[][] ParseBoard(String FEN) {
        String fenBoard = FEN.split("\\s+")[0];
        Piece[][] board =
                new Piece[Board.MaxBoardRow - Board.MinBoardRow + 1][Board.MaxBoardColumn - Board.MinBoardColumn + 1];

        int rank = Board.MaxBoardRow;
        int file = Board.MinBoardColumn;
        for (int i = 0; i < fenBoard.length(); i++) {
            char c = fenBoard.charAt(i);
            if (c == '/') {
                rank--;
                file = Board.MinBoardColumn;
            } else {
                if (Character.isDigit(c)) {
                    file += Character.digit(c, 10);
                } else {
                    board[rank][file] = charToPieceTranslation.get(c).apply(new Position(rank, file));
                    file++;
                }
            }
        }

        return board;
    }

    /**
     * Get the player from the FEN string.
     * @param FEN The FEN string to parse.
     * @return The player parsed from the FEN string.
     */
    public static Player getCurrentPlayer(String FEN) {
        String currentPlayer = FEN.split("\\s+")[1];

        return currentPlayer.equals("w") ? Player.WHITE : Player.BLACK;
    }

    /**
     * Get the number of moves from the FEN string.
     * @param FEN The FEN string to parse.
     * @return The move count parsed from the FEN string.
     */
    public static int getMoves(String FEN) {
        String moves = FEN.split("\\s+")[5];

        return Integer.parseInt(moves);
    }

    /**
     * Get the number of half-moves from the FEN string.
     * @param FEN The FEN string to parse.
     * @return The half-move number parsed from the FEN string.
     */
    public static int getHalfMoves(String FEN) {
        String halfmoves = FEN.split("\\s+")[4];

        return Integer.parseInt(halfmoves);
    }

    /**
     * Get the en passant tile from the FEN string.
     * @param FEN The FEN string to parse.
     * @return The en passant tile from the FEN string.
     */
    public static Position getEnPassant(String FEN) {
        String enPassant = FEN.split("\\s+")[3];

        return enPassant.equals("-") ? null : new Position(enPassant);
    }

    /**
     * Create a FEN string from the given state of the match
     * @param board The board situation in the match
     * @param currentPlayer The player whose turn it is
     * @param possibleCastling The possible castling options
     * @param halfmoveClock The number of halfmoves since the last pawn move or capture
     * @param moves The number of full moves since the start of the game
     * @param enpassant The enpassant square
     * @return The FEN string
     */
    public static String FENStringFromBoard(
            Board board, Player currentPlayer, Set<Castling> possibleCastling, int halfmoveClock, int moves, Position enpassant
    ) {
        StringBuilder builder = new StringBuilder();
        addBoardToBuilder(builder, board);

        builder.append(' ');

        if (currentPlayer == Player.WHITE) {
            builder.append('w');
        } else {
            builder.append('b');
        }

        builder.append(' ');

        if (possibleCastling.contains(Castling.WhiteKingside))
            builder.append('K');
        if (possibleCastling.contains(Castling.WhiteQueenside))
            builder.append('Q');
        if (possibleCastling.contains(Castling.BlackKingside))
            builder.append('k');
        if (possibleCastling.contains(Castling.BlackQueenside))
            builder.append('q');

        builder.append(' ');

        if (enpassant != null) {
            builder.append(enpassant.getNotation());
        } else {
            builder.append('-');
        }

        builder.append(' ');
        builder.append(halfmoveClock);

        builder.append(' ');
        builder.append(moves);

        return builder.toString();
    }

    /**
     * Add the board to the string builder
     * @param builder The string builder to add the board to
     * @param board The board to add
     */
    private static void addBoardToBuilder(StringBuilder builder, Board board) {
        for (int rank = Board.MaxBoardRow; rank >= Board.MinBoardRow; --rank) {
            int emptySquares = 0;
            for (int file = Board.MinBoardColumn; file <= Board.MaxBoardColumn; ++file) {
                if (board.getPiece(rank, file) == null) {
                    emptySquares++;
                } else {
                    if (emptySquares > 0) {
                        builder.append(emptySquares + '0');
                        emptySquares = 0;
                    }

                    Piece piece = board.getPiece(rank, file);
                    builder.append(pieceToCharTranslation.get(piece.getType()).apply(piece.color));
                }
            }
            if (emptySquares > 0) {
                builder.append(emptySquares + '0');
            }
            if (rank > Board.MinBoardRow) {
                builder.append('/');
            }
        }
    }
}
