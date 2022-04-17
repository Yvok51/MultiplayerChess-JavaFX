package multiplayerchess.multiplayerchess.server.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.Move;

import java.util.List;

public abstract class Piece {
    public final Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public abstract Movement getMovement();

    public abstract Movement getCaptureMovement();

    public abstract List<Move> generateMoveList(Position start, boolean isCapture);

    public abstract PieceType getType();

    public Piece getMovedPiece() {
        return this;
    }
}
