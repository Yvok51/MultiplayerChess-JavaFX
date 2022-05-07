package multiplayerchess.multiplayerchess.common;

/**
 * The base FEN parser which has methods used by both the client and server parser.
 */
public class BaseFENParser {

    /**
     * Gets the player from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The player parsed from the FEN string.
     */
    public static Player getCurrentPlayer(String FEN) {
        String currentPlayer = FEN.split("\\s+")[1];

        return currentPlayer.equals("w") ? Player.WHITE : Player.BLACK;
    }

    /**
     * Gets the number of moves from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The move count parsed from the FEN string.
     */
    public static int getMoves(String FEN) {
        String moves = FEN.split("\\s+")[5];

        return Integer.parseInt(moves);
    }

    /**
     * Gets the number of half-moves from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The half-move number parsed from the FEN string.
     */
    public static int getHalfMoves(String FEN) {
        String halfmoves = FEN.split("\\s+")[4];

        return Integer.parseInt(halfmoves);
    }

    /**
     * Gets the en passant tile from the FEN string.
     *
     * @param FEN The FEN string to parse.
     * @return The en passant tile from the FEN string.
     */
    public static Position getEnPassant(String FEN) {
        String enPassant = FEN.split("\\s+")[3];

        return enPassant.equals("-") ? null : new Position(enPassant);
    }
}
