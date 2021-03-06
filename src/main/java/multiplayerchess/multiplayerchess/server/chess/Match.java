package multiplayerchess.multiplayerchess.server.chess;

import multiplayerchess.multiplayerchess.common.*;
import multiplayerchess.multiplayerchess.server.chess.parsing.FENParser;
import multiplayerchess.multiplayerchess.server.chess.pieces.Castling;
import multiplayerchess.multiplayerchess.server.chess.pieces.Piece;
import multiplayerchess.multiplayerchess.server.chess.rules.ChessRules;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a chess match with the board as well as extra information needed to play the game.
 */
public final class Match {

    private static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    // private static final String STALEMATE_FEN = "7k/8/8/5Q2/8/8/4K3/8 w - - 0 1";
    // private static final String INSUFFICIENT_MATERIAL_FEN = "8/8/7k/8/6p1/4N3/4K3/8 w - - 0 1";

    private final Board board;
    private final ChessRules rules;
    private final Set<Castling> possibleCastles;
    private int moves;
    private int halfmoveClock;
    private Player currentPlayer;
    private Position enPassant;

    /**
     * The Match constructor. Creates a new match from the StartingFen string
     */
    public Match() {
        String FENToUse = START_FEN;
        board = new Board(FENToUse);
        rules = new ChessRules();
        moves = FENParser.getMoves(FENToUse);
        halfmoveClock = FENParser.getHalfMoves(FENToUse);
        currentPlayer = FENParser.getCurrentPlayer(FENToUse);
        enPassant = FENParser.getEnPassant(FENToUse);

        possibleCastles = new HashSet<>();
        possibleCastles.add(Castling.WHITE_KINGSIDE);
        possibleCastles.add(Castling.WHITE_QUEENSIDE);
        possibleCastles.add(Castling.BLACK_KINGSIDE);
        possibleCastles.add(Castling.BLACK_QUEENSIDE);
    }

    /**
     * Performs the given move in the match if it is legal.
     *
     * @param move Move to perform
     * @return Whether the move was legal and was thus performed
     */
    public boolean makeMove(Move move) {
        if (!rules.isMoveValid(board, move, currentPlayer, enPassant, possibleCastles)) {
            return false;
        }

        Piece movedPiece = board.getPiece(move.startPosition);

        // Update move count
        if (currentPlayer == Player.BLACK) {
            moves++;
        }

        // Update halfmove clock
        if (move.isCapture || move.pieceType == PieceType.PAWN) {
            halfmoveClock = 0;
        } else {
            halfmoveClock++;
        }

        performMove(move);

        // Flip current player
        currentPlayer = currentPlayer.opposite();
        // check whether we made a two tile move with a pawn -> en passant possible
        enPassant = nextEnPassant(move, movedPiece);
        removeCastlingOptions(move, movedPiece);

        return true;
    }

    /**
     * Gets the FEN representation of the current state of the match
     *
     * @return The FEN string representation
     */
    public String getFEN() {
        return FENParser.FENStringFromBoard(board, currentPlayer, possibleCastles, halfmoveClock, moves, enPassant);
    }

    /**
     * Gets the player whose turn it is to move
     *
     * @return The player whose turn it is to move
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Answers whether the game is over in the current state
     *
     * @return Whether the game ended
     */
    public boolean gameOver() {
        // Possible ways a chess match can end
        // win - checkmate
        // win - resignation - not handled here
        // draw - stalemate
        // draw - 50 move rule
        // draw - insufficient material
        // draw - ~~three-fold repetition~~
        // draw - ~~dead position~~
        return halfmoveClock > 50
                || rules.insufficientMaterial(board)
                || rules.checkMate(board, currentPlayer, enPassant)
                || rules.stalemate(board, currentPlayer, enPassant);
    }

    /**
     * Returns the winner of the match, if there is one
     *
     * @return The whether of the match
     */
    public Optional<Player> winner() {
        if (rules.checkMate(board, currentPlayer, enPassant)) {
            return Optional.of(currentPlayer.opposite());
        }

        return Optional.empty();
    }

    /**
     * Makes the move,
     * assumes the move has been validated beforehand i.e. piece at the stated position is not null, all the fields
     * are valid, etc.
     *
     * @param move The move to perform
     */
    private void performMove(Move move) {
        Piece movedPiece = board.getPiece(move.startPosition);

        // Check whether move is a castle
        if (rules.isCastle(board, move, currentPlayer, possibleCastles)) {
            performCastle(board, move);
        }
        // Check whether move is an en passant
        else if (rules.isEnPassant(board, move, enPassant)) {
            performEnPassant(board, move, movedPiece);
        }
        // Normal move
        else {
            performNormalMove(board, move, movedPiece);
        }
    }

    /**
     * Performs castle moves
     * Presumes the move is valid and that the king is signified as the moved piece
     *
     * @param board The current board
     * @param move  The move to be performed
     */
    private void performCastle(Board board, Move move) {
        Position whiteShortCastlePosition = new Position(ChessRules.MinBoardRow, ChessRules.MinBoardColumn + 6);
        Position whiteLongCastlePosition = new Position(ChessRules.MinBoardRow, ChessRules.MinBoardColumn + 2);
        Position blackShortCastlePosition = new Position(ChessRules.MaxBoardRow, ChessRules.MinBoardColumn + 6);
        Position blackLongCastlePosition = new Position(ChessRules.MaxBoardRow, ChessRules.MinBoardColumn + 2);

        // move both king and rook
        Action4<Position, Position, Position, Position> moveKingRook = (kingPosition, kingFinalPosition, rookPosition, rookFinalPosition) -> {
            board.setPiece(kingFinalPosition, rules.pieceAfterMove(board.getPiece(kingPosition), false));
            board.clearPiece(kingPosition);
            board.setPiece(rookFinalPosition, rules.pieceAfterMove(board.getPiece(rookPosition), false));
            board.clearPiece(rookPosition);
        };

        if (move.endPosition.equals(whiteShortCastlePosition)) {
            Position kingPosition = new Position(ChessRules.MinBoardRow, 4);
            Position rookPosition = new Position(ChessRules.MinBoardRow, ChessRules.MaxBoardColumn);
            Position rookFinalPosition = new Position(ChessRules.MinBoardRow, 5);
            moveKingRook.call(kingPosition, move.endPosition, rookPosition, rookFinalPosition);
        } else if (move.endPosition.equals(whiteLongCastlePosition)) {
            Position kingPosition = new Position(ChessRules.MinBoardRow, 4);
            Position rookPosition = new Position(ChessRules.MinBoardRow, ChessRules.MinBoardColumn);
            Position rookFinalPosition = new Position(ChessRules.MinBoardRow, 3);
            moveKingRook.call(kingPosition, move.endPosition, rookPosition, rookFinalPosition);
        } else if (move.endPosition.equals(blackShortCastlePosition)) {
            Position kingPosition = new Position(ChessRules.MaxBoardRow, 4);
            Position rookPosition = new Position(ChessRules.MaxBoardRow, ChessRules.MaxBoardColumn);
            Position rookFinalPosition = new Position(ChessRules.MaxBoardRow, 5);
            moveKingRook.call(kingPosition, move.endPosition, rookPosition, rookFinalPosition);
        } else if (move.endPosition.equals(blackLongCastlePosition)) {
            Position kingPosition = new Position(ChessRules.MaxBoardRow, 4);
            Position rookPosition = new Position(ChessRules.MaxBoardRow, ChessRules.MinBoardColumn);
            Position rookFinalPosition = new Position(ChessRules.MaxBoardRow, 3);
            moveKingRook.call(kingPosition, move.endPosition, rookPosition, rookFinalPosition);
        }
    }

    /**
     * Performs en passant moves
     * Presumes the move is valid
     *
     * @param board      The current board
     * @param move       The move to be performed
     * @param movedPiece The piece that is to be moved
     */
    private void performEnPassant(Board board, Move move, Piece movedPiece) {
        int pawnRowOffset = movedPiece.color == Color.WHITE ? -1 : 1;
        board.setPiece(move.endPosition, rules.pieceAfterMove(movedPiece, rules.isPromotion(move, movedPiece, currentPlayer)));
        board.clearPiece(move.startPosition);
        board.clearPiece(new Position(move.endPosition.row + pawnRowOffset, move.endPosition.column));
    }

    /**
     * Performs a standard move that is not handled by the other 'perform' functions,
     * see {@link #performCastle(Board, Move) PerformCastle} and
     * {@link #performEnPassant(Board, Move, Piece) PerformEnPassant}
     * Presumes the move is valid
     *
     * @param board      The current board
     * @param move       The move to be performed
     * @param movedPiece The piece that is to be moved
     */
    private void performNormalMove(Board board, Move move, Piece movedPiece) {
        // Check whether the move is a promotion in rules.pieceAfterMove
        board.setPiece(move.endPosition, rules.pieceAfterMove(movedPiece, rules.isPromotion(move, movedPiece, currentPlayer)));
        board.clearPiece(move.startPosition);
    }

    /**
     * Determines the en passant that will be possible in the next ply/half-move
     * Presumes the move is valid
     *
     * @param move       The move that has happened
     * @param movedPiece The piece that has been moved
     * @return The possible en passant in the next ply/half-move, returns null if no en passant is possible next ply
     */
    private Position nextEnPassant(Move move, Piece movedPiece) {
        Position nextEnPassant = null;
        if (move.isEnPassant()) {
            int rowOffset = movedPiece.color == Color.WHITE ? -1 : 1;
            nextEnPassant = new Position(move.endPosition.row + rowOffset, move.endPosition.column);
        }

        return nextEnPassant;
    }

    /**
     * Removes the castling options that were discarded by the current move
     *
     * @param move       The move that has happened
     * @param movedPiece The piece that was moved
     */
    private void removeCastlingOptions(Move move, Piece movedPiece) {

        if (movedPiece.getType() == PieceType.KING) {
            if (movedPiece.color == Color.WHITE) {
                possibleCastles.remove(Castling.WHITE_KINGSIDE);
                possibleCastles.remove(Castling.WHITE_QUEENSIDE);
            } else {
                possibleCastles.remove(Castling.BLACK_KINGSIDE);
                possibleCastles.remove(Castling.BLACK_QUEENSIDE);
            }
        }
        if (movedPiece.getType() == PieceType.ROOK) {
            if (movedPiece.color == Color.WHITE) {
                Position whiteKingsideRookPosition = new Position(ChessRules.MinBoardRow, ChessRules.MaxBoardColumn);
                Position whiteQueensideRookPosition = new Position(ChessRules.MinBoardRow, ChessRules.MinBoardColumn);
                if (whiteKingsideRookPosition.equals(move.startPosition)) {
                    possibleCastles.remove(Castling.WHITE_KINGSIDE);
                } else if (whiteQueensideRookPosition.equals(move.startPosition)) {
                    possibleCastles.remove(Castling.WHITE_QUEENSIDE);
                }
            } else {
                Position blackKingsideRookPosition = new Position(ChessRules.MaxBoardRow, ChessRules.MaxBoardColumn);
                Position blackQueensideRookPosition = new Position(ChessRules.MaxBoardRow, ChessRules.MinBoardColumn);
                if (blackKingsideRookPosition.equals(move.startPosition)) {
                    possibleCastles.remove(Castling.BLACK_KINGSIDE);
                } else if (blackQueensideRookPosition.equals(move.startPosition)) {
                    possibleCastles.remove(Castling.BLACK_QUEENSIDE);
                }
            }
        }
    }
}
