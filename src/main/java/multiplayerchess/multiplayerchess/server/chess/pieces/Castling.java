package multiplayerchess.multiplayerchess.server.chess.pieces;

/**
 * The enumeration of all the possible castling moves.
 */
public enum Castling {
    WHITE_KINGSIDE(0b0001),
    WHITE_QUEENSIDE(0b0010),
    BLACK_KINGSIDE(0b0100),
    BLACK_QUEENSIDE(0b1000);

    public final int id;

    Castling(int id) {
        this.id = id;
    }
}
