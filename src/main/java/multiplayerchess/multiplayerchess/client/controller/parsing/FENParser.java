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
        builder.put('k', () -> new King(Color.BLACK));
        builder.put('K', () -> new King(Color.WHITE));
        builder.put('p', () -> new Pawn(Color.BLACK));
        builder.put('P', () -> new Pawn(Color.WHITE));
        builder.put('n', () -> new Knight(Color.BLACK));
        builder.put('N', () -> new Knight(Color.WHITE));
        builder.put('b', () -> new Bishop(Color.BLACK));
        builder.put('B', () -> new Bishop(Color.WHITE));
        builder.put('r', () -> new Rook(Color.BLACK));
        builder.put('R', () -> new Rook(Color.WHITE));
        builder.put('q', () -> new Queen(Color.BLACK));
        builder.put('Q', () -> new Queen(Color.WHITE));

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
                    /* For some reason the FEN string we get has the digits representing the number of empty squares
                     * converted to its value?!? So we have to convert it back to the original number, which this
                     * little hack does. */
                    if (Character.isDigit(fenBoard.charAt(i + 1))) {
                        i += 1;
                        char secondDigit = fenBoard.charAt(i);
                        char originalChar = (char) (Character.digit(c, 10) * 10 + Character.digit(secondDigit, 10));
                        file += Character.digit(originalChar, 10);
                    }
                    else {
                        file += Character.digit(c, 10);
                    }
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

        return currentPlayer.equals("w") ? Player.WHITE : Player.BLACK;
    }
}
