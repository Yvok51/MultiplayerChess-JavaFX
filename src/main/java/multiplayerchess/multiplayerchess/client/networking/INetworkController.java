package multiplayerchess.multiplayerchess.client.networking;

import multiplayerchess.multiplayerchess.client.controller.Match;
import multiplayerchess.multiplayerchess.common.Color;
import multiplayerchess.multiplayerchess.common.PieceType;
import multiplayerchess.multiplayerchess.common.Position;
import multiplayerchess.multiplayerchess.common.messages.ServerOngoingMatchMessage;

import java.util.Optional;

public interface INetworkController {
    public Optional<Match> StartMatch();
    public Optional<Match> joinMatch(String matchID);
    public boolean sendTurn(PieceType pieceType, Position startPosition,
                            Position endPosition, Color color, boolean isCapture, String matchID);
    Optional<ServerOngoingMatchMessage> receiveTurnReply();
}
