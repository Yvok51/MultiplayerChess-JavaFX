package multiplayerchess.multiplayerchess.common;

import java.io.Serializable;

public enum Player implements Serializable {
    BLACK,
    WHITE;

    static final long serialVersionUID = 0x98765;

    public Player opposite() {
        return this == Player.WHITE ? Player.BLACK : Player.WHITE;
    }

    public Color getColor() {
        return this == Player.WHITE ? Color.WHITE : Color.BLACK;
    }
}
