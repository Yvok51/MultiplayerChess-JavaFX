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

public class FENParser {
    private static final Map<PieceType, Function<Color, Character>> pieceToCharTransaltion;
    private static final Map<Character, Function<Position, Piece>> charToPieceTransaltion;

    static {
        Map<PieceType, Function<Color, Character>> builder = new HashMap<>();
        builder.put(PieceType.King, (color) -> color == Color.White ? 'K' : 'k');
        builder.put(PieceType.Queen, (color) -> color == Color.White ? 'Q' : 'q');
        builder.put(PieceType.Bishop, (color) -> color == Color.White ? 'B' : 'b');
        builder.put(PieceType.Knight, (color) -> color == Color.White ? 'N' : 'n');
        builder.put(PieceType.Rook, (color) -> color == Color.White ? 'R' : 'r');
        builder.put(PieceType.Pawn, (color) -> color == Color.White ? 'P' : 'p');

        pieceToCharTransaltion = Collections.unmodifiableMap(builder);

        Map<Character, Function<Position, Piece>> pieceBuilder = new HashMap<>();
        pieceBuilder.put('k', (position) -> new King(Color.Black));
        pieceBuilder.put('K', (position) -> new King(Color.White));
        pieceBuilder.put('p', (position) -> {
            return position.row == 6 ? new Pawn(Color.Black, false) : new Pawn(Color.Black, true);
        });
        pieceBuilder.put('P', (position) -> {
            return position.row == 1 ? new Pawn(Color.White, false) : new Pawn(Color.White, true);
        });
        pieceBuilder.put('n', (position) -> new Knight(Color.Black));
        pieceBuilder.put('N', (position) -> new Knight(Color.White));
        pieceBuilder.put('b', (position) -> new Bishop(Color.Black));
        pieceBuilder.put('B', (position) -> new Bishop(Color.White));
        pieceBuilder.put('r', (position) -> new Rook(Color.Black));
        pieceBuilder.put('R', (position) -> new Rook(Color.White));
        pieceBuilder.put('q', (position) -> new Queen(Color.Black));
        pieceBuilder.put('Q', (position) -> new Queen(Color.White));

        charToPieceTransaltion = Collections.unmodifiableMap(pieceBuilder);
    }

    public static Piece[][] ParseBoard(String FEN) {
        String fenBoard = FEN.split("\\s+")[0];
        Piece[][] board = new Piece[Board.MaxBoardRow - Board.MinBoardRow + 1][Board.MaxBoardColumn - Board.MinBoardColumn + 1];

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
                    board[rank][file] = charToPieceTransaltion.get(c).apply(new Position(rank, file));
                    file++;
                }
            }
        }

        return board;
    }

    public static Player getCurrentPlayer(String FEN) {
        String currentPlayer = FEN.split("\\s+")[1];

        return currentPlayer.equals("w") ? Player.White : Player.Black;
    }

    public static String FENStringFromBoard(
            Board board, Player currentPlayer, Set<Castling> possibleCastling, int halfmoveClock, int moves, Position enpassant
    ) {
        StringBuilder builder = new StringBuilder();
        addBoardToBuilder(builder, board);

        builder.append(' ');

        if (currentPlayer == Player.White) {
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

        if (enpassant != Position.InvalidPosition) {
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
                    builder.append(pieceToCharTransaltion.get(piece.getType()).apply(piece.color));
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
