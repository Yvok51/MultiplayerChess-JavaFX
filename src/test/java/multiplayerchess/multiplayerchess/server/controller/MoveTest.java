package multiplayerchess.multiplayerchess.server.controller;

import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    void getMovePathKnight() {
        Move move = new Move(PieceType.KNIGHT, new Position(0, 0), new Position(2, 1), false);
        List<Position> path = move.getMovePath();
        List<Position> expectedPath = List.of();

        assertEquals(expectedPath, path);
    }

    @Test
    void getMovePathRook() {
        Move move = new Move(PieceType.ROOK, new Position(0, 0), new Position(5, 0), false);
        List<Position> path = move.getMovePath();
        List<Position> expectedPath = List.of(new Position(1, 0), new Position(2, 0),
                new Position(3, 0), new Position(4, 0));

        assertEquals(expectedPath, path);
    }
}