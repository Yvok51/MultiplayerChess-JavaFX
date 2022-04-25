package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.common.*;
import multiplayerchess.multiplayerchess.common.messages.ServerOngoingMatchMessage;

import java.util.Optional;

public class MockNetworkController implements INetworkController{

    @Override
    public Optional<Match> StartMatch() {
        return Optional.of(new Match(Match.NormalStartingFEN, Player.White, Networking.DEBUG_MATCH_ID));
    }

    @Override
    public Optional<Match> joinMatch(String matchID) {
        return Optional.of(new Match(Match.NormalStartingFEN, Player.Black, Networking.DEBUG_MATCH_ID));
    }

    @Override
    public boolean sendTurn(PieceType pieceType, Position startPosition, Position endPosition, Color color, boolean isCapture, String matchID) {
        return true;
    }

    @Override
    public Optional<ServerOngoingMatchMessage> receiveTurnReply() {
        return Optional.empty();
    }
}
