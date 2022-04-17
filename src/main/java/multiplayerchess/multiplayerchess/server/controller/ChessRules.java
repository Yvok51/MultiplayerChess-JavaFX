package multiplayerchess.multiplayerchess.server.controller;

import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Player;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.server.controller.pieces.Castling;
import multiplayerchess.multiplayerchess.server.controller.pieces.Piece;
import multiplayerchess.multiplayerchess.server.controller.pieces.Queen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChessRules {

    public static final int MinBoardRow = 0;
    public static final int MaxBoardRow = 7;
    public static final int MinBoardColumn = 0;
    public static final int MaxBoardColumn = 7;
    public static final int RowCount = MaxBoardRow + 1;
    public static final int ColumnCount = MaxBoardColumn + 1;

    /**
     * Whether the move is legal in the current board state
     *
     * @param board           The current board
     * @param move            The move that is to be checked
     * @param currentPlayer   The player whose turn it is
     * @param enPassant       The possible en passant move in the current ply (half-move)
     * @param possibleCastles All the possible castles that can be performed
     * @return Whether the given move is valid in the current state of the board
     */
    public boolean isMoveValid(Board board, Move move, Player currentPlayer, Position enPassant, Set<Castling> possibleCastles) {
        Piece piece = board.getPiece(move.startPosition);
        if (piece == null || colorToPlayer(piece.color) != currentPlayer) {
            return false;
        }

        List<Move> possibleMoves = generatePossibleMovesForPiece(board, move.startPosition, currentPlayer, move.isCapture, enPassant);
        return isCastle(board, move, currentPlayer, possibleCastles, enPassant) || possibleMoves.contains(move);
    }

    /**
     * Returned the piece which has moved - marks the piece as moved
     *
     * @param piece       The piece that has moved
     * @param isPromotion Whether the piece is to be promoted
     * @return Return the piece after being moved
     */
    public Piece pieceAfterMove(Piece piece, boolean isPromotion) {
        return isPromotion && piece.getType() == PieceType.Pawn ? new Queen(piece.color) : piece.getMovedPiece();
    }

    /**
     * Whether the current move results in a promotion
     *
     * @param move          The move that is checked
     * @param movedPiece    The piece that is to be moved
     * @param currentPlayer The player whose turn it is
     * @return Whether the current move results in a promotion
     */
    public boolean isPromotion(Move move, Piece movedPiece, Player currentPlayer) {
        return (currentPlayer == Player.White && movedPiece.getType() == PieceType.Pawn
                && move.endPosition.row == ChessRules.MaxBoardRow) ||
                (currentPlayer == Player.Black && movedPiece.getType() == PieceType.Pawn
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
        return move.isCapture && board.getPiece(move.startPosition).getType() == PieceType.Pawn && enPassant.equals(move.endPosition);
    }

    /**
     * Check whether the given move is a castle move and whether it is valid as well
     *
     * @param board           The current board
     * @param move            The move that is to be checked
     * @param currentPlayer   The player whose turn it is
     * @param possibleCastles All the possible castles that can be performed
     * @param enPassant       The possible en passant move in the current ply (half-move)
     * @return Whether the move is a valid castle move
     */
    public boolean isCastle(Board board, Move move, Player currentPlayer, Set<Castling> possibleCastles, Position enPassant) {
        var movedPiece = board.getPiece(move.startPosition);
        if (move.pieceType != PieceType.King || movedPiece == null || movedPiece.getType() != PieceType.King) {
            return false;
        }

        Position whiteKingStartingPosition = new Position(MinBoardRow, MinBoardColumn + 4);
        Position blackKingStartingPosition = new Position(MaxBoardRow, MinBoardColumn + 4);

        Position whiteShortCastlePosition = new Position(MinBoardRow, MinBoardColumn + 6);
        Position whiteLongCastlePosition = new Position(MinBoardRow, MinBoardColumn + 2);
        Position blackShortCastlePosition = new Position(MaxBoardRow, MinBoardColumn + 6);
        Position blackLongCastlePosition = new Position(MaxBoardRow, MinBoardColumn + 2);

        // TODO: Refactor
        if (currentPlayer == Player.White && move.startPosition.equals(whiteKingStartingPosition) && whiteShortCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(new Position(MinBoardRow, MinBoardColumn + 5), whiteShortCastlePosition));
            boolean castlePossible = possibleCastles.contains(Castling.WhiteKingside);
            boolean kingInDanger = TilesAreThreatened(board, kingsMoveTiles, currentPlayer, enPassant);
            boolean pathFree = anyPieceInPath(board, kingsMoveTiles);
            return castlePossible && !kingInDanger && pathFree;
        }
        if (currentPlayer == Player.White && move.startPosition.equals(whiteKingStartingPosition) && whiteLongCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(new Position(MinBoardRow, MinBoardColumn + 3), whiteLongCastlePosition));
            boolean castlePossible = possibleCastles.contains(Castling.WhiteQueenside);
            boolean kingInDanger = TilesAreThreatened(board, kingsMoveTiles, currentPlayer, enPassant);
            kingsMoveTiles.add(new Position(MinBoardRow, MinBoardColumn + 1));
            boolean pathFree = anyPieceInPath(board, kingsMoveTiles);
            return castlePossible && !kingInDanger && pathFree;
        }
        if (currentPlayer == Player.Black && move.startPosition.equals(blackKingStartingPosition) && blackShortCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(new Position(MaxBoardRow, MinBoardColumn + 5), blackShortCastlePosition));
            boolean castlePossible = possibleCastles.contains(Castling.BlackKingside);
            boolean kingInDanger = TilesAreThreatened(board, kingsMoveTiles, currentPlayer, enPassant);
            boolean pathFree = anyPieceInPath(board, kingsMoveTiles);
            return castlePossible && !kingInDanger && pathFree;
        }
        if (currentPlayer == Player.Black && move.startPosition.equals(blackKingStartingPosition) && blackLongCastlePosition.equals(move.endPosition)) {
            List<Position> kingsMoveTiles = new ArrayList<>(List.of(new Position(MaxBoardRow, MinBoardColumn + 3), blackLongCastlePosition));
            boolean castlePossible = possibleCastles.contains(Castling.BlackQueenside);
            boolean kingInDanger = TilesAreThreatened(board, kingsMoveTiles, currentPlayer, enPassant);
            kingsMoveTiles.add(new Position(MaxBoardRow, MinBoardColumn + 1));
            boolean pathFree = anyPieceInPath(board, kingsMoveTiles);
            return castlePossible && !kingInDanger && pathFree;
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
        var whitePiecePositions = getPlayerPiecePositions(board, Player.White);
        var blackPiecePositions = getPlayerPiecePositions(board, Player.Black);

        if (whitePiecePositions.size() + blackPiecePositions.size() > 3) {
            return false;
        }

        if (whitePiecePositions.size() == 1 && blackPiecePositions.size() == 1) {
            return true;
        }

        if (whitePiecePositions.size() > 1) {
            var firstPiece = board.getPiece(whitePiecePositions.get(0));
            var secondPiece = board.getPiece(whitePiecePositions.get(1));
            var otherPiece = firstPiece.getType() != PieceType.King ? firstPiece : secondPiece;
            return otherPiece.getType() == PieceType.Knight || otherPiece.getType() == PieceType.Bishop;
        } else {
            var firstPiece = board.getPiece(blackPiecePositions.get(0));
            var secondPiece = board.getPiece(blackPiecePositions.get(1));
            var otherPiece = firstPiece.getType() != PieceType.King ? firstPiece : secondPiece;
            return otherPiece.getType() == PieceType.Knight || otherPiece.getType() == PieceType.Bishop;
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
        boolean kingInCheck = kingIsInCheck(board, playersTurn, enPassant);
        return kingInCheck && playersMoves.size() == 0;
    }

    /**
     * Answers whether the stalemate happened
     *
     * @param board       The current board
     * @param playersTurn The player whose turn it is
     * @param enPassant   The possible en passant move in the current ply (half-move)
     * @return Whether a stalemate occurred
     */
    public boolean stalemate(Board board, Player playersTurn, Position enPassant) {
        var playersMoves = generatePlayerPossibleMoves(board, playersTurn, enPassant);
        boolean kingInCheck = kingIsInCheck(board, playersTurn, enPassant);
        return !kingInCheck && playersMoves.size() == 0;
    }

    /**
     * Generate all the possible moves of a player
     *
     * @param board     The current board
     * @param player    The players whose moves we are searching for
     * @param enPassant The possible en passant move in the current ply (half-move)
     * @return The list of all moves that the given player can make
     */
    private List<Move> generatePlayerPossibleMoves(Board board, Player player, Position enPassant) {
        List<Move> moves = new ArrayList<>();

        for (var position : getPlayerPiecePositions(board, player)) {
            moves.addAll(generatePossibleMovesForPiece(board, position, player, true, enPassant));
            moves.addAll(generatePossibleMovesForPiece(board, position, player, false, enPassant));
        }

        return moves;
    }

    /**
     * Generates all possible moves for the piece on the given position
     *
     * @param board         The current board
     * @param piecePosition The position of the piece we are generating moves for
     * @param player        The player whose turn it is
     * @param isCapture     Decides whether the moves will be captures - if true - only captures are included, if false - only non-captures
     * @param enPassant     The possible en passant move in the current ply (half-move)
     * @return List of possible moves
     */
    private List<Move> generatePossibleMovesForPiece(Board board, Position piecePosition, Player player, boolean isCapture, Position enPassant) {
        Piece piece = board.getPiece(piecePosition);

        return removeInvalidMoves(board, piece.generateMoveList(piecePosition, isCapture), player, enPassant);
    }

    private Player colorToPlayer(Color color) {
        return color == Color.White ? Player.White : Player.Black;
    }

    private Color playerToColor(Player player) {
        return player == Player.White ? Color.White : Color.Black;
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
    private List<Move> removeInvalidMoves(Board board, List<Move> moves, Player player, Position enPassant) {
        List<Move> validMoves = new ArrayList<>();
        for (Move move : moves) {
            if (anyPieceInPath(board, move)
                    && isValidDestination(board, move, enPassant)
                    && !kingIsInCheckAfterMove(board, move, player, enPassant)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Get the positions of all the player's pieces
     *
     * @param board  The current board
     * @param player The player whose pieces we search for
     * @return The list of positions of all of the player's pieces
     */
    private List<Position> getPlayerPiecePositions(Board board, Player player) {
        List<Position> positions = new ArrayList<>();
        Color desiredPieceColor = playerToColor(player);

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
     * @param board     The board before the move
     * @param move      The moved to be performed on the board
     * @param player    The player whose king we are checking
     * @param enPassant The possible en passant possible as the next move (the one we are about to check)
     * @return Whether the given player's king will be in check after the move
     */
    private boolean kingIsInCheckAfterMove(Board board, Move move, Player player, Position enPassant) {
        var movedPiece = board.getPiece(move.startPosition);
        var originalPiece = board.getPiece(move.endPosition);
        // perform move
        board.setPiece(move.endPosition, movedPiece);
        board.clearPiece(move.startPosition);

        boolean isInCheck = kingIsInCheck(board, player, enPassant);

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
     * @param enPassant The possible en passant in the current move
     * @return Whether the player's king is in check
     */
    private boolean kingIsInCheck(Board board, Player whoseKing, Position enPassant) {
        return TileIsThreatened(board, getKingsPosition(board, whoseKing), whoseKing, enPassant);
    }

    /**
     * Get the position of the player's king on the board.
     * Presumes the board is valid, i.e. there is only one king
     *
     * @param board     The board where to look
     * @param whoseKing The player whose king we are looking for
     * @return The position of the player's king on the board
     */
    private Position getKingsPosition(Board board, Player whoseKing) {
        Color desiredKingColor = playerToColor(whoseKing);

        for (int row = MinBoardRow; row < RowCount; row++) {
            for (int column = MinBoardColumn; column < ColumnCount; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece != null && piece.color.equals(desiredKingColor) && piece.getType() == PieceType.King) {
                    return new Position(row, column);
                }
            }
        }

        return null;
    }

    /**
     * Answers whether the given tile is threatened by the pieces of the given player
     *
     * @param board     The board to use
     * @param tile      The tile to test whether it is threatened
     * @param player    Whose piece's to check whether they threaten the tile
     * @param enPassant The current en passant possible on the board
     * @return Whether the tile is threatened by the pieces of the player
     */
    private boolean TileIsThreatened(Board board, Position tile, Player player, Position enPassant) {
        return generatePlayerPossibleMoves(board, player, enPassant).stream().parallel().anyMatch(
                move -> move.isCapture && move.endPosition.equals(tile)
        );
    }

    /**
     * Answers whether ANY of the given tiles are threatened by the pieces of the given player
     *
     * @param board     The board to use
     * @param tiles     The tiles to test whether they are threatened
     * @param player    Whose piece's to check whether they threaten the tiles
     * @param enPassant The current en passant possible on the board
     * @return Whether ANY of the tile are threatened by the pieces of the player
     */
    private boolean TilesAreThreatened(Board board, List<Position> tiles, Player player, Position enPassant) {
        return generatePlayerPossibleMoves(board, player, enPassant).stream().parallel().anyMatch(
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
    private boolean anyPieceInPath(Board board, List<Position> path) {
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
    private boolean anyPieceInPath(Board board, Move move) {
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
    private boolean isValidDestination(Board board, Move move, Position enPassant) {
        Piece piece = board.getPiece(move.startPosition);
        Piece capturedPiece = board.getPiece(move.endPosition);

        if (capturedPiece == null) {
            if (isEnPassant(piece, move, enPassant)) {
                return true;
            }
            return !move.isCapture;
        }

        return piece.color != capturedPiece.color;
    }

    /**
     * Answer whether the move is en passant
     *
     * @param movedPiece The piece moved
     * @param move       The move to check
     * @param enPassant  The possible en passant in the current move
     * @return Is the move an en passant move
     */
    private boolean isEnPassant(Piece movedPiece, Move move, Position enPassant) {
        return move.isCapture && movedPiece.getType() == PieceType.Pawn && enPassant.equals(move.endPosition);
    }
}