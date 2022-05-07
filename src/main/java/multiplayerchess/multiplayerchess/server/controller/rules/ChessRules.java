package multiplayerchess.multiplayerchess.server.controller.rules;

import multiplayerchess.multiplayerchess.common.*;
import multiplayerchess.multiplayerchess.server.controller.Board;
import multiplayerchess.multiplayerchess.server.controller.Move;
import multiplayerchess.multiplayerchess.server.controller.pieces.Castling;
import multiplayerchess.multiplayerchess.server.controller.pieces.Piece;
import multiplayerchess.multiplayerchess.server.controller.pieces.Queen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Methods which answer rule questions about the game.
 */
public class ChessRules {

    public static final int MinBoardRow = 0;
    public static final int MaxBoardRow = 7;
    public static final int MinBoardColumn = 0;
    public static final int MaxBoardColumn = 7;
    public static final int RowCount = MaxBoardRow + 1;
    public static final int ColumnCount = MaxBoardColumn + 1;

    /**
     * Answers whether the move is legal in the current board state
     *
     * @param board           The current board
     * @param move            The move that is to be checked
     * @param currentPlayer   The player whose turn it is
     * @param enPassant       The possible en passant move in the current ply (half-move)
     * @param possibleCastles All the possible castles that can be performed
     * @return Whether the given move is valid in the current state of the board
     */
    public boolean isMoveValid(Board board, Move move, Player currentPlayer,
                               Position enPassant, Set<Castling> possibleCastles) {
        Piece piece = board.getPiece(move.startPosition);
        if (piece == null || piece.color.getPlayer() != currentPlayer) {
            return false;
        }

        List<Move> possibleMoves = generatePossibleMovesForPiece(board, move.startPosition, currentPlayer, move.isCapture, enPassant);
        return isCastle(board, move, currentPlayer, possibleCastles) || possibleMoves.contains(move);
    }

    /**
     * Returns the piece which has moved - marks the piece as moved
     *
     * @param piece       The piece that has moved
     * @param isPromotion Whether the piece is to be promoted
     * @return Return the piece after being moved
     */
    public Piece pieceAfterMove(Piece piece, boolean isPromotion) {
        return isPromotion && piece.getType() == PieceType.PAWN ? new Queen(piece.color) : piece.getMovedPiece();
    }

    /**
     * Answers whether the current move results in a promotion
     *
     * @param move          The move that is checked
     * @param movedPiece    The piece that is to be moved
     * @param currentPlayer The player whose turn it is
     * @return Whether the current move results in a promotion
     */
    public boolean isPromotion(Move move, Piece movedPiece, Player currentPlayer) {
        return (currentPlayer == Player.WHITE && movedPiece.getType() == PieceType.PAWN
                && move.endPosition.row == ChessRules.MaxBoardRow) ||
                (currentPlayer == Player.BLACK && movedPiece.getType() == PieceType.PAWN
                        && move.endPosition.row == ChessRules.MinBoardRow);
    }

    /**
     * Answers whether the move is an en passant move
     *
     * @param board     The current board
     * @param move      The move that is to be performed and is checked whether it is an en passant move
     * @param enPassant The possible en passant move in the current ply (half-move)
     * @return Whether the move is an en passant move
     */
    public boolean isEnPassant(Board board, Move move, Position enPassant) {
        return isEnPassant(board.getPiece(move.startPosition), move, enPassant);
    }

    /**
     * Checks whether the given move is a castle move and whether it is valid as well
     *
     * @param board           The current board
     * @param move            The move that is to be checked
     * @param currentPlayer   The player whose turn it is
     * @param possibleCastles All the possible castles that can be performed
     * @return Whether the move is a valid castle move
     */
    public boolean isCastle(Board board, Move move, Player currentPlayer, Set<Castling> possibleCastles) {
        var movedPiece = board.getPiece(move.startPosition);
        if (move.pieceType != PieceType.KING || movedPiece == null || movedPiece.getType() != PieceType.KING) {
            return false;
        }

        // Cannot castle while in check
        if (kingIsInCheck(board, currentPlayer)) {
            return false;
        }

        Position whiteKingStartingPosition = new Position(MinBoardRow, MinBoardColumn + 4);
        Position blackKingStartingPosition = new Position(MaxBoardRow, MinBoardColumn + 4);

        Position whiteShortCastlePosition = new Position(whiteKingStartingPosition.row, whiteKingStartingPosition.column + 2);
        Position whiteLongCastlePosition = new Position(whiteShortCastlePosition.row, whiteKingStartingPosition.column - 2);
        Position blackShortCastlePosition = new Position(blackKingStartingPosition.row, blackKingStartingPosition.column + 2);
        Position blackLongCastlePosition = new Position(blackShortCastlePosition.row, blackKingStartingPosition.column - 2);

        Func4<Castling, List<Position>, List<Position>, Boolean> isCastle = (castling, kingMoves, pathToBeOpen) -> {
            boolean castlePossible = possibleCastles.contains(castling);
            boolean kingInDanger = TilesAreThreatened(board, kingMoves, currentPlayer.opposite());
            boolean pathBlocked = anyPieceInPath(board, pathToBeOpen);
            return castlePossible && !kingInDanger && !pathBlocked;
        };

        if (currentPlayer == Player.WHITE && move.startPosition.equals(whiteKingStartingPosition) && whiteShortCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(
                    new Position(whiteShortCastlePosition.row, whiteShortCastlePosition.column - 1),
                    whiteShortCastlePosition));

            return isCastle.call(Castling.WHITE_KINGSIDE, kingsMoveTiles, kingsMoveTiles);
        }
        if (currentPlayer == Player.WHITE && move.startPosition.equals(whiteKingStartingPosition) && whiteLongCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(
                    new Position(whiteLongCastlePosition.row, whiteLongCastlePosition.column + 1),
                    whiteLongCastlePosition));
            List<Position> pathToBeOpen = new ArrayList<>(kingsMoveTiles);
            pathToBeOpen.add(new Position(whiteLongCastlePosition.row, whiteLongCastlePosition.column - 1));

            return isCastle.call(Castling.WHITE_QUEENSIDE, kingsMoveTiles, pathToBeOpen);
        }
        if (currentPlayer == Player.BLACK && move.startPosition.equals(blackKingStartingPosition) && blackShortCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(
                    new Position(blackKingStartingPosition.row, blackShortCastlePosition.column - 1),
                    blackShortCastlePosition));

            return isCastle.call(Castling.BLACK_KINGSIDE, kingsMoveTiles, kingsMoveTiles);
        }
        if (currentPlayer == Player.BLACK && move.startPosition.equals(blackKingStartingPosition) && blackLongCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(
                    new Position(blackLongCastlePosition.row, blackLongCastlePosition.column + 1),
                    blackLongCastlePosition));
            List<Position> pathToBeOpen = new ArrayList<>(kingsMoveTiles);
            pathToBeOpen.add(new Position(blackKingStartingPosition.row, blackKingStartingPosition.column - 1));

            return isCastle.call(Castling.BLACK_QUEENSIDE, kingsMoveTiles, pathToBeOpen);
        }

        return false;
    }

    /**
     * Answers whether there is insufficient material on the board to continue the match (a draw is certain)
     *
     * @param board The current board
     * @return Whether there is insufficient material
     */
    public boolean insufficientMaterial(Board board) {
        var whitePiecePositions = getPlayerPiecePositions(board, Player.WHITE);
        var blackPiecePositions = getPlayerPiecePositions(board, Player.BLACK);

        if (whitePiecePositions.size() + blackPiecePositions.size() > 3) {
            return false;
        }

        if (whitePiecePositions.size() == 1 && blackPiecePositions.size() == 1) {
            return true;
        }

        if (whitePiecePositions.size() > 1) {
            var firstPiece = board.getPiece(whitePiecePositions.get(0));
            var secondPiece = board.getPiece(whitePiecePositions.get(1));
            var otherPiece = firstPiece.getType() != PieceType.KING ? firstPiece : secondPiece;
            return otherPiece.getType() == PieceType.KNIGHT || otherPiece.getType() == PieceType.BISHOP;
        } else {
            var firstPiece = board.getPiece(blackPiecePositions.get(0));
            var secondPiece = board.getPiece(blackPiecePositions.get(1));
            var otherPiece = firstPiece.getType() != PieceType.KING ? firstPiece : secondPiece;
            return otherPiece.getType() == PieceType.KNIGHT || otherPiece.getType() == PieceType.BISHOP;
        }
    }

    /**
     * Answers whether the player is mated
     *
     * @param board       The current board
     * @param playersTurn The player whose turn it is
     * @param enPassant   The possible en passant move in the current ply (half-move)
     * @return Whether the given player is mated
     */
    public boolean checkMate(Board board, Player playersTurn, Position enPassant) {
        var playersMoves = generatePlayerPossibleMoves(board, playersTurn, enPassant);
        boolean kingInCheck = kingIsInCheck(board, playersTurn);
        return kingInCheck && playersMoves.size() == 0;
    }

    /**
     * Answers whether the stalemate happened.
     *
     * @param board       The current board
     * @param playersTurn The player whose turn it is
     * @param enPassant   The possible en passant move in the current ply (half-move)
     * @return Whether a stalemate occurred
     */
    public boolean stalemate(Board board, Player playersTurn, Position enPassant) {
        var playersMoves = generatePlayerPossibleMoves(board, playersTurn, enPassant);
        boolean kingInCheck = kingIsInCheck(board, playersTurn);
        return !kingInCheck && playersMoves.size() == 0;
    }

    /**
     * Generates all the possible moves of a player.
     *
     * @param board     The current board
     * @param player    The players whose moves we are searching for
     * @param enPassant The possible en passant move in the current ply (half-move)
     * @return The list of all moves that the given player can make
     */
    List<Move> generatePlayerPossibleMoves(Board board, Player player, Position enPassant) {
        List<Move> moves = new ArrayList<>();

        for (var position : getPlayerPiecePositions(board, player)) {
            moves.addAll(generatePossibleMovesForPiece(board, position, player, true, enPassant));
            moves.addAll(generatePossibleMovesForPiece(board, position, player, false, enPassant));
        }

        return moves;
    }

    /**
     * Generates all moves for a piece, even ones not possible due to king safety or whether it is a capture.
     *
     * @param board  The current board
     * @param player The player whose moves we are searching for
     * @return The list of all moves that the given player can make
     */
    List<Move> generatePlayerAllMoves(Board board, Player player) {
        List<Move> moves = new ArrayList<>();

        for (var position : getPlayerPiecePositions(board, player)) {
            moves.addAll(generateAllMovesForPiece(board, position));
            moves.addAll(generateAllMovesForPiece(board, position));
        }

        return moves;
    }

    /**
     * Generates all valid moves for the piece on the given position. Respects king safety and whether it is a capture.
     *
     * @param board         The current board
     * @param piecePosition The position of the piece we are generating moves for
     * @param player        The player whose turn it is
     * @param isCapture     Decides whether the moves will be captures - if true - only captures are included, if false - only non-captures
     * @param enPassant     The possible en passant move in the current ply (half-move)
     * @return List of possible moves
     */
    List<Move> generatePossibleMovesForPiece(Board board, Position piecePosition, Player player,
                                             boolean isCapture, Position enPassant) {
        Piece piece = board.getPiece(piecePosition);
        List<Move> allMoves = piece.generateMoveList(piecePosition, isCapture);    // List of all possible moves for the piece
        return removeInvalidMoves(board, allMoves, player, enPassant);
    }

    /**
     * Generates all possible moves for the piece on the given position.
     *
     * @param board         The current board
     * @param piecePosition The position of the piece we are generating moves for
     * @return List of possible moves
     */
    List<Move> generateAllMovesForPiece(Board board, Position piecePosition) {
        Piece piece = board.getPiece(piecePosition);
        // List of all possible moves for the piece
        List<Move> allMoves = piece.generateMoveList(piecePosition, true);
        allMoves.addAll(piece.generateMoveList(piecePosition, false));
        return removeBlockedMoves(board, allMoves);
    }

    /**
     * Filters out any moves that don't follow the rules of the game, or moves which contradict their own isCapture
     * field (are captures, but aren't mark as such and vice versa), or moves which put us in check
     *
     * @param board     The current board
     * @param moves     The list of moves to filter
     * @param player    The player whose move it is
     * @param enPassant The possible en passant move in the current ply (half-move)
     * @return Filtered list of legal moves
     */
    List<Move> removeInvalidMoves(Board board, List<Move> moves, Player player,
                                  Position enPassant) {
        return moves.stream().filter((move) ->
                !anyPieceInPath(board, move) && isValidDestination(board, move, enPassant)
                        && !kingIsInCheckAfterMove(board, move, player)
        ).toList();
    }

    /**
     * Removes moves which are blocked by another piece
     *
     * @param board The current board
     * @param moves The list of moves to filter
     * @return Filtered list of legal moves
     */
    List<Move> removeBlockedMoves(Board board, List<Move> moves) {
        return moves.stream().filter((move) -> !anyPieceInPath(board, move)).toList();
    }

    /**
     * Gets the positions of all the player's pieces
     *
     * @param board  The current board
     * @param player The player whose pieces we search for
     * @return The list of positions of all the player's pieces
     */
    List<Position> getPlayerPiecePositions(Board board, Player player) {
        List<Position> positions = new ArrayList<>();
        Color desiredPieceColor = player.getColor();

        for (int row = MinBoardRow; row < RowCount; row++) {
            for (int column = MinBoardColumn; column < ColumnCount; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece != null && piece.color.equals(desiredPieceColor)) {
                    positions.add(new Position(row, column));
                }
            }
        }

        return positions;
    }

    /**
     * Answers whether after the move is performed on the board, the king of the specified player will be in check
     *
     * @param board  The board before the move
     * @param move   The moved to be performed on the board
     * @param player The player whose king we are checking
     * @return Whether the given player's king will be in check after the move
     */
    boolean kingIsInCheckAfterMove(Board board, Move move, Player player) {
        var movedPiece = board.getPiece(move.startPosition);
        var originalPiece = board.getPiece(move.endPosition);
        // perform move
        board.setPiece(move.endPosition, movedPiece);
        board.clearPiece(move.startPosition);

        boolean isInCheck = kingIsInCheck(board, player);

        // revert move
        board.setPiece(move.startPosition, movedPiece);
        board.setPiece(move.endPosition, originalPiece);

        return isInCheck;
    }

    /**
     * Answers whether the player's king is in check
     *
     * @param board     The board to check on
     * @param whoseKing Player whose king we are looking at for the check
     * @return Whether the player's king is in check
     */
    boolean kingIsInCheck(Board board, Player whoseKing) {
        return TileIsThreatened(board, getKingsPosition(board, whoseKing), whoseKing.opposite());
    }

    /**
     * Gets the position of the player's king on the board.
     * Presumes the board is valid, i.e. there is only one king
     *
     * @param board     The board where to look
     * @param whoseKing The player whose king we are looking for
     * @return The position of the player's king on the board
     */
    Position getKingsPosition(Board board, Player whoseKing) {
        Color desiredKingColor = whoseKing.getColor();

        for (int row = MinBoardRow; row < RowCount; row++) {
            for (int column = MinBoardColumn; column < ColumnCount; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece != null && piece.color.equals(desiredKingColor) && piece.getType() == PieceType.KING) {
                    return new Position(row, column);
                }
            }
        }

        return null;
    }

    /**
     * Answers whether the given tile is threatened by the pieces of the given player
     *
     * @param board  The board to use
     * @param tile   The tile to test whether it is threatened
     * @param player Whose piece to check whether they threaten the tile
     * @return Whether the tile is threatened by the pieces of the player
     */
    boolean TileIsThreatened(Board board, Position tile, Player player) {
        return generatePlayerAllMoves(board, player).stream().parallel().anyMatch(
                move -> move.isCapture && move.endPosition.equals(tile)
        );
    }

    /**
     * Answers whether ANY of the given tiles are threatened by the pieces of the given player
     *
     * @param board  The board to use
     * @param tiles  The tiles to test whether they are threatened
     * @param player Whose piece's to check whether they threaten the tiles
     * @return Whether ANY of the tile are threatened by the pieces of the player
     */
    boolean TilesAreThreatened(Board board, List<Position> tiles, Player player) {
        return generatePlayerAllMoves(board, player).stream().parallel().anyMatch(
                move -> move.isCapture && tiles.contains(move.endPosition)
        );
    }

    /**
     * Checks whether any pieces are in the path
     *
     * @param board The board to use
     * @param path  The tiles that make up the path
     * @return Whether any pieces are located on the path
     */
    boolean anyPieceInPath(Board board, List<Position> path) {
        for (var tile : path) {
            if (board.getPiece(tile) != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether any pieces are in the path of teh given move
     *
     * @param board The board to use
     * @param move  The move whose path we are checking
     * @return Whether any pieces are located on the path of the move
     */
    boolean anyPieceInPath(Board board, Move move) {
        return anyPieceInPath(board, move.getMovePath());
    }

    /**
     * Checks whether we can move on the square given by endPosition and the isCapture field is correct
     *
     * @param board     The board to use
     * @param move      The move whose destination to check
     * @param enPassant The en passant possible in the current move
     * @return Whether the end destination of the move is valid
     */
    boolean isValidDestination(Board board, Move move, Position enPassant) {
        Piece piece = board.getPiece(move.startPosition);
        Piece capturedPiece = board.getPiece(move.endPosition);

        if (capturedPiece == null) {
            if (isEnPassant(piece, move, enPassant)) {
                return true;
            }
            return !move.isCapture;
        }

        return piece.color != capturedPiece.color && move.isCapture;
    }

    /**
     * Answers whether the move is en passant
     *
     * @param movedPiece The piece moved
     * @param move       The move to check
     * @param enPassant  The possible en passant in the current move
     * @return Is the move an en passant move
     */
    boolean isEnPassant(Piece movedPiece, Move move, Position enPassant) {
        return movedPiece != null && move.isCapture && movedPiece.getType() == PieceType.PAWN &&
                movedPiece.getType() == move.pieceType && move.endPosition.equals(enPassant);
    }
}
