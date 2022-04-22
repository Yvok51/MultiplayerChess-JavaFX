package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.common.Networking;
import multiplayerchess.multiplayerchess.common.Player;

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
}
