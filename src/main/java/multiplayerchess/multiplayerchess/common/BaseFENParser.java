package multiplayerchess.multiplayerchess.common;

public class BaseFENParser {

    /**
     * Get the player from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The player parsed from the FEN string.
     */
    public static Player getCurrentPlayer(String FEN) {
        String currentPlayer = FEN.split("\\s+")[1];

        return currentPlayer.equals("w") ? Player.WHITE : Player.BLACK;
    }

    /**
     * Get the number of moves from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The move count parsed from the FEN string.
     */
    public static int getMoves(String FEN) {
        String moves = FEN.split("\\s+")[5];

        return Integer.parseInt(moves);
    }

    /**
     * Get the number of half-moves from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The half-move number parsed from the FEN string.
     */
    public static int getHalfMoves(String FEN) {
        String halfmoves = FEN.split("\\s+")[4];

        return Integer.parseInt(halfmoves);
    }

    /**
     * Get the en passant tile from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The en passant tile from the FEN string.
     */
    public static Position getEnPassant(String FEN) {
        String enPassant = FEN.split("\\s+")[3];

        return enPassant.equals("-") ? null : new Position(enPassant);
    }
}
