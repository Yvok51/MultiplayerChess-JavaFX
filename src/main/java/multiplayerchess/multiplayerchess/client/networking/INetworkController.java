package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.client.controller.Match;

import java.util.Optional;

public interface INetworkController {
    public Optional<Match> StartMatch();
    public Optional<Match> joinMatch(String matchID);
}
