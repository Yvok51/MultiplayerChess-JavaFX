package multiplayerchess.multiplayerchess.server.controller.pieces;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.Move;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    @Test
    void whitePawnHasNotMovedAndIsNotCaptureGenerateMoveListTest() {
        Piece pawn = new Pawn(Color.WHITE, false);
        Position start = new Position(1, 1);
        boolean isCaptureMove = false;

        var moveList = pawn.generateMoveList(start, isCaptureMove);
        List<Move> expectedMoveList = List.of(
                new Move(PieceType.PAWN, start, new Position(2, 1), isCaptureMove),
                new Move(PieceType.PAWN, start, new Position(3, 1), isCaptureMove));

        assertEquals(expectedMoveList, moveList);
    }

    @Test
    void blackPawnHasNotMovedAndIsNotCaptureGenerateMoveListTest() {
        Piece pawn = new Pawn(Color.BLACK, false);
        Position start = new Position(6, 1);
        boolean isCaptureMove = false;

        var moveList = pawn.generateMoveList(start, isCaptureMove);
        List<Move> expectedMoveList = List.of(
                new Move(PieceType.PAWN, start, new Position(5, 1), isCaptureMove),
                new Move(PieceType.PAWN, start, new Position(4, 1), isCaptureMove));

        assertEquals(expectedMoveList, moveList);
    }

    @Test
    void whitePawnHasMovedAndIsNotCaptureGenerateMoveListTest() {
        Piece pawn = new Pawn(Color.WHITE, true);
        Position start = new Position(1, 1);
        boolean isCaptureMove = false;

        var moveList = pawn.generateMoveList(start, isCaptureMove);
        List<Move> expectedMoveList = List.of(
                new Move(PieceType.PAWN, start, new Position(2, 1), isCaptureMove));

        assertEquals(expectedMoveList, moveList);
    }

    @Test
    void blackPawnHasMovedAndIsNotCaptureGenerateMoveListTest() {
        Piece pawn = new Pawn(Color.BLACK, true);
        Position start = new Position(6, 1);
        boolean isCaptureMove = false;

        var moveList = pawn.generateMoveList(start, isCaptureMove);
        List<Move> expectedMoveList = List.of(
                new Move(PieceType.PAWN, start, new Position(5, 1), isCaptureMove));

        assertEquals(expectedMoveList, moveList);
    }

    @Test
    void whitePawnIsCapturingGenerateMoveListTest() {
        Piece pawn = new Pawn(Color.WHITE, true);
        Position start = new Position(1, 1);
        boolean isCaptureMove = true;

        var moveList = pawn.generateMoveList(start, isCaptureMove);
        List<Move> expectedMoveList = List.of(
                new Move(PieceType.PAWN, start, new Position(2, 0), isCaptureMove),
                new Move(PieceType.PAWN, start, new Position(2, 2), isCaptureMove));

        assertEquals(expectedMoveList, moveList);
    }

    @Test
    void blackPawnIsCapturingGenerateMoveListTest() {
        Piece pawn = new Pawn(Color.BLACK, true);
        Position start = new Position(1, 1);
        boolean isCaptureMove = true;

        var moveList = pawn.generateMoveList(start, isCaptureMove);
        List<Move> expectedMoveList = List.of(
                new Move(PieceType.PAWN, start, new Position(0, 0), isCaptureMove),
                new Move(PieceType.PAWN, start, new Position(0, 2), isCaptureMove));

        assertEquals(expectedMoveList, moveList);
    }

    @Test
    void getType() {
        Piece pawn = new Pawn(Color.WHITE, false);

        assertEquals(pawn.getType(), PieceType.PAWN);
    }

}