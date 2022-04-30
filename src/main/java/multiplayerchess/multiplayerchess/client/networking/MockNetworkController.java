package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.common.*;
import multiplayerchess.multiplayerchess.common.messages.ServerOngoingMatchMessage;

import java.util.Optional;

/**
 * Mock implementation of NetworkController.
 */
public class MockNetworkController implements INetworkController{

    @Override
    public Optional<Match> StartMatch() {
        return Optional.of(new Match(Match.NormalStartingFEN, Player.WHITE, Networking.DEBUG_MATCH_ID));
    }

    @Override
    public Optional<Match> joinMatch(String matchID) {
        return Optional.of(new Match(Match.NormalStartingFEN, Player.BLACK, Networking.DEBUG_MATCH_ID));
    }

    @Override
    public Optional<TurnReply> sendTurn(PieceType pieceType, Position startPosition, Position endPosition,
            Color color, boolean isCapture, String matchID) {
        return Optional.empty();
    }

    @Override
    public Optional<ServerOngoingMatchMessage> receiveTurnReply() {
        return Optional.empty();
    }
}
