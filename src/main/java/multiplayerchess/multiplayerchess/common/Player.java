package multiplayerchess.multiplayerchess.common;

import java.io.Serializable;

public enum Player implements Serializable {
    Black,
    White;

    static final long serialVersionUID = 0x98765;

    public Player opposite() {
        return this == Player.White ? Player.Black : Player.White;
    }
}
