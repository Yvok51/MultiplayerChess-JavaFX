package multiplayerchess.multiplayerchess.client.controller.parsing;

import multiplayerchess.multiplayerchess.client.controller.pieces.*;
import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.client.controller.Board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FENParser {
    private static final Map<Character, Supplier<Piece>> pieceTransaltion;

    static {
        Map<Character, Supplier<Piece>> builder = new HashMap<>();
        builder.put('k', () -> new King(Color.Black));
        builder.put('K', () -> new King(Color.White));
        builder.put('p', () -> new Pawn(Color.Black));
        builder.put('P', () -> new Pawn(Color.White));
        builder.put('n', () -> new Knight(Color.Black));
        builder.put('N', () -> new Knight(Color.White));
        builder.put('b', () -> new Bishop(Color.Black));
        builder.put('B', () -> new Bishop(Color.White));
        builder.put('r', () -> new Rook(Color.Black));
        builder.put('R', () -> new Rook(Color.White));
        builder.put('q', () -> new Queen(Color.Black));
        builder.put('Q', () -> new Queen(Color.White));

        pieceTransaltion = Collections.unmodifiableMap(builder);

    }

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
                    board[rank][file] = pieceTransaltion.get(c).get();
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
}
