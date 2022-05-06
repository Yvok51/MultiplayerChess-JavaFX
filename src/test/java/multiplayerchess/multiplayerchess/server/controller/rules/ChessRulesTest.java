package multiplayerchess.multiplayerchess.server.controller.rules;

import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.Board;
import multiplayerchess.multiplayerchess.server.controller.Move;
import multiplayerchess.multiplayerchess.server.controller.parsing.FENParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChessRulesTest {

    private ChessRules chessRules;

    private static final String emptyBoardFEN = "8/8/8/8/8/8/8/8 w - - 0 1";
    private static final String beginningFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String earlyGameFEN = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
    private static final String midGameFEN = "r4rk1/2p1bpp1/2nqbn1p/4p3/3pP2P/p2P1PPB/2PQN3/R1B1K2R w KQ - 2 19";
    private static final String endGameFEN = "7R/kp2R3/2p5/8/2pPp3/P3P1r1/K1P5/8 w - - 0 52";
    private static final String enPassantFEN = "rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3";
    private static final String castleFEN = "r3k3/8/8/4B2b/8/8/8/R3K2R w KQq - 0 1";

    @BeforeEach
    void setUp() {
        chessRules = new ChessRules();
    }

    @AfterEach
    void tearDown() { chessRules = null; }

    @Test
    void generatePlayerPossibleMoves() {
        Board board = new Board(castleFEN);

        List<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(PieceType.KING, new Position(7, 4), new Position(7, 5), false));
        expectedMoves.add(new Move(PieceType.KING, new Position(7, 4), new Position(7, 3), false));
        expectedMoves.add(new Move(PieceType.KING, new Position(7, 4), new Position(6, 3), false));
        expectedMoves.add(new Move(PieceType.KING, new Position(7, 4), new Position(6, 4), false));
        expectedMoves.add(new Move(PieceType.KING, new Position(7, 4), new Position(6, 5), false));

        expectedMoves.add(new Move(PieceType.BISHOP, new Position(4, 7), new Position(3, 6), false));
        expectedMoves.add(new Move(PieceType.BISHOP, new Position(4, 7), new Position(2, 5), false));
        expectedMoves.add(new Move(PieceType.BISHOP, new Position(4, 7), new Position(1, 4), false));
        expectedMoves.add(new Move(PieceType.BISHOP, new Position(4, 7), new Position(0, 3), false));
        expectedMoves.add(new Move(PieceType.BISHOP, new Position(4, 7), new Position(5, 6), false));
        expectedMoves.add(new Move(PieceType.BISHOP, new Position(4, 7), new Position(6, 5), false));

        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(7, 1), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(7, 2), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(7, 3), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(6, 0), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(5, 0), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(4, 0), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(3, 0), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(2, 0), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(1, 0), false));
        expectedMoves.add(new Move(PieceType.ROOK, new Position(7, 0), new Position(0, 0), true));

        List<Move> actualMoves = chessRules.generatePlayerPossibleMoves(board, Player.BLACK, null);

        assertTrue(actualMoves.containsAll(expectedMoves) && expectedMoves.containsAll(actualMoves) && actualMoves.size() == expectedMoves.size());
    }

    @Test
    void isCastleCastleNotBlockedByThreatOnlyOnRook() {
        Board board = new Board(castleFEN);
        Move baseCastle = new Move(PieceType.KING, new Position(7, 4), new Position(7, 2), false);

        assertTrue(chessRules.isCastle(board, baseCastle, Player.BLACK, FENParser.getCastling(castleFEN)));
    }

    @Test
    void isCastleCastleBlockedByThreat() {
        Board board = new Board(castleFEN);
        Move baseCastle = new Move(PieceType.KING, new Position(0, 4), new Position(0, 2), false);

        boolean isCastle = chessRules.isCastle(board, baseCastle, Player.WHITE, FENParser.getCastling(castleFEN));
        assertFalse(isCastle);
    }

    @Test
    void isCastleBaseTest() {
        Board board = new Board(castleFEN);
        Move baseCastle = new Move(PieceType.KING, new Position(0, 4), new Position(0, 6), false);

        assertTrue(chessRules.isCastle(board, baseCastle, Player.WHITE, FENParser.getCastling(castleFEN)));
    }

    @Test
    void isMoveValidEnPassant() {
        Board board = new Board(enPassantFEN);
        Move enPassant = new Move(PieceType.PAWN, new Position(4, 4), new Position(5, 5), true);

        boolean isValid = chessRules.isMoveValid(board, enPassant, Player.WHITE, FENParser.getEnPassant(enPassantFEN), Set.of());
        assertTrue(isValid);
    }

    @Test
    void isMoveValidWrongMove() {
        Board board = new Board(beginningFEN);

        Move pawnMove = new Move(PieceType.PAWN, new Position(6, 4), new Position(5, 5), false);

        assertFalse(chessRules.isMoveValid(board, pawnMove, Player.WHITE, null, Set.of()));
    }

    @Test
    void isMoveValidWrongPlayer() {
        Board board = new Board(beginningFEN);

        Move pawnMove = new Move(PieceType.PAWN, new Position(6, 4), new Position(5, 4), false);
        Move pawnDoubleMove = new Move(PieceType.PAWN, new Position(6, 4), new Position(4, 4), false);
        Move knightMove = new Move(PieceType.PAWN, new Position(7, 1), new Position(5, 2), false);

        assertFalse(chessRules.isMoveValid(board, pawnMove, Player.WHITE, null, Set.of()));
        assertFalse(chessRules.isMoveValid(board, pawnDoubleMove, Player.WHITE, null, Set.of()));
        assertFalse(chessRules.isMoveValid(board, knightMove, Player.WHITE, null, Set.of()));
    }

    @Test
    void isMoveValidTrue() {
        Board board = new Board(beginningFEN);

        Move pawnMove = new Move(PieceType.PAWN, new Position(6, 4), new Position(5, 4), false);
        Move pawnDoubleMove = new Move(PieceType.PAWN, new Position(6, 4), new Position(4, 4), false);
        Move knightMove = new Move(PieceType.KNIGHT, new Position(7, 1), new Position(5, 2), false);

        assertTrue(chessRules.isMoveValid(board, pawnMove, Player.BLACK, null, Set.of()));
        assertTrue(chessRules.isMoveValid(board, pawnDoubleMove, Player.BLACK, null, Set.of()));
        assertTrue(chessRules.isMoveValid(board, knightMove, Player.BLACK, null, Set.of()));
    }

    @Test
    void generatePossibleMovesForPieceCannotIntoCheck() {
        Board board = new Board(endGameFEN);

        List<Move> kingPossibleMoves = chessRules.generatePossibleMovesForPiece(
                board, new Position(6, 0), Player.BLACK, false, null);
        List<Move> kingExpectedMoves = List.of(
                new Move(PieceType.KING, new Position(6, 0), new Position(5, 0), false),
                new Move(PieceType.KING, new Position(6, 0), new Position(5, 1), false)
        );

        List<Move> pawnPossibleMoves = chessRules.generatePossibleMovesForPiece(
                board, new Position(6, 1), Player.BLACK, false, null);
        List<Move> pawnExpectedMoves = List.of();

        assertEquals(kingExpectedMoves, kingPossibleMoves);
        assertEquals(pawnExpectedMoves, pawnPossibleMoves);
    }
/*
    @Test
    void generatePossibleMovesForPieceCastling() {
        Board board = new Board(midGameFEN);

        List<Move> notCapturePossibleMoves = chessRules.generatePossibleMovesForPiece(
                board, new Position(0, 4), Player.WHITE, false, null);
        List<Move> notCaptureExpectedMoves = List.of(
                new Move(PieceType.PAWN, new Position(0, 4), new Position(0, 3), false),
                new Move(PieceType.PAWN, new Position(0, 4), new Position(0, 5), false),
                new Move(PieceType.PAWN, new Position(0, 4), new Position(0, 6), false),
                new Move(PieceType.PAWN, new Position(0, 4), new Position(1, 5), false)
        );

        List<Move> capturePossibleMoves = chessRules.generatePossibleMovesForPiece(
                board, new Position(1, 4), Player.WHITE, true, null);
        List<Move> captureExpectedMoves = List.of();

        assertEquals(notCaptureExpectedMoves, notCapturePossibleMoves);
        assertEquals(captureExpectedMoves, capturePossibleMoves);
    }
*/
    @Test
    void generatePossibleMovesForPiece() {
        Board board = new Board(beginningFEN);

        List<Move> notCapturePossibleMoves = chessRules.generatePossibleMovesForPiece(
                board, new Position(1, 4), Player.WHITE, false, null);
        List<Move> notCaptureExpectedMoves = List.of(
                new Move(PieceType.PAWN, new Position(1, 4), new Position(2, 4), false),
                new Move(PieceType.PAWN, new Position(1, 4), new Position(3, 4), false)
        );

        List<Move> capturePossibleMoves = chessRules.generatePossibleMovesForPiece(
                board, new Position(1, 4), Player.WHITE, true, null);
        List<Move> captureExpectedMoves = List.of();

        assertEquals(notCaptureExpectedMoves, notCapturePossibleMoves);
        assertEquals(captureExpectedMoves, capturePossibleMoves);
    }

    @Test
    void getPlayerPiecePositions() {
        Board board = new Board(beginningFEN);
        List<Position> whitePlayerPiecePositions = chessRules.getPlayerPiecePositions(board, Player.WHITE);
        List<Position> blackPlayerPiecePositions = chessRules.getPlayerPiecePositions(board, Player.BLACK);

        List<Position> whiteExpectedPositions = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                whiteExpectedPositions.add(new Position(i, j));
            }
        }

        List<Position> blackExpectedPositions = new ArrayList<>();
        for (int i = 6; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                blackExpectedPositions.add(new Position(i, j));
            }
        }

        assertEquals(whiteExpectedPositions, whitePlayerPiecePositions);
        assertEquals(blackExpectedPositions, blackPlayerPiecePositions);
    }

    @Test
    void emptyBoardGetPlayerPiecePositions() {
        Board board = new Board(emptyBoardFEN);
        List<Position> whitePlayerPiecePositions = chessRules.getPlayerPiecePositions(board, Player.WHITE);
        List<Position> blackPlayerPiecePositions = chessRules.getPlayerPiecePositions(board, Player.BLACK);

        assertEquals(0, whitePlayerPiecePositions.size());
        assertEquals(0, blackPlayerPiecePositions.size());
    }

    @Test
    void getKingsPosition() {
        Board board = new Board(beginningFEN);
        Position whiteKingPosition = chessRules.getKingsPosition(board, Player.WHITE);
        Position blackKingPosition = chessRules.getKingsPosition(board, Player.BLACK);

        assertEquals(new Position(0, 4), whiteKingPosition);
        assertEquals(new Position(7, 4), blackKingPosition);
    }

    @Test
    void getKingsPositionMidGame() {
        Board board = new Board(midGameFEN);
        Position whiteKingPosition = chessRules.getKingsPosition(board, Player.WHITE);
        Position blackKingPosition = chessRules.getKingsPosition(board, Player.BLACK);

        assertEquals(new Position(0, 4), whiteKingPosition);
        assertEquals(new Position(7, 6), blackKingPosition);
    }

    @Test
    void anyPieceInPathClearPath() {
        Board board = new Board(midGameFEN);
        Move move = new Move(PieceType.BISHOP, new Position(5, 4), new Position(1, 0), false);
        assertFalse(chessRules.anyPieceInPath(board, move));
    }

    @Test
    void anyPieceInPathNotClear() {
        Board board = new Board(midGameFEN);
        Move move = new Move(PieceType.BISHOP, new Position(2, 7), new Position(6, 3), false);
        assertTrue(chessRules.anyPieceInPath(board, move));
    }

    @Test
    void isValidDestinationNotCapture() {
        Board board = new Board(beginningFEN);
        board.clearPiece(new Position(1, 0));
        Move testMove = new Move(PieceType.ROOK, new Position(0, 0), new Position(5, 0), false);

        assertTrue(chessRules.isValidDestination(board, testMove, null));
    }

    @Test
    void isValidDestinationFalseNotCapture() {
        Board board = new Board(beginningFEN);
        board.clearPiece(new Position(1, 0));
        Move testMove = new Move(PieceType.ROOK, new Position(0, 0), new Position(6, 0), false);

        assertFalse(chessRules.isValidDestination(board, testMove, null));
    }

    @Test
    void isValidDestinationCapture() {
        Board board = new Board(beginningFEN);
        board.clearPiece(new Position(1, 0));
        Move testMove = new Move(PieceType.ROOK, new Position(0, 0), new Position(6, 0), true);

        assertTrue(chessRules.isValidDestination(board, testMove, null));
    }

    @Test
    void isValidDestinationFalseCapture() {
        Board board = new Board(beginningFEN);
        board.clearPiece(new Position(1, 0));
        Move testMove = new Move(PieceType.ROOK, new Position(0, 0), new Position(4, 0), true);

        assertFalse(chessRules.isValidDestination(board, testMove, null));
    }

    @Test
    void isValidDestinationEnPassant() {
        Board board = new Board(beginningFEN);
        Move testMove = new Move(PieceType.PAWN, new Position(1, 0), new Position(2, 1), true);

        boolean result = chessRules.isValidDestination(board, testMove, new Position(2, 1));
        assertTrue(result);
    }

}