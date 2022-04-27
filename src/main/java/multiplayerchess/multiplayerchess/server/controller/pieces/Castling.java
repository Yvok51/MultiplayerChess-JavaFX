package multiplayerchess.multiplayerchess.server.controller.pieces;

/**
 * The enumeration of all the possible castling moves.
 */
public enum Castling {
    WhiteKingside(0b0001),
    WhiteQueenside(0b0010),
    BlackKingside(0b0100),
    BlackQueenside(0b1000);

    public final int id;

    Castling(int id) {
        this.id = id;
    }
}
