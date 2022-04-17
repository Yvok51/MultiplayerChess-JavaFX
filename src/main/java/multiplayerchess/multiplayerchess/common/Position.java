package multiplayerchess.multiplayerchess.common;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public final class Position implements Serializable {

    public static final Position InvalidPosition = new Position(-1, -1);
    static final long serialVersionUID = 0x12345;
    private static final Map<Character, Integer> columnTransaltion = Map.of(
            'a', 0,
            'b', 1,
            'c', 2,
            'd', 3,
            'e', 4,
            'f', 5,
            'g', 6,
            'h', 7
    );
    public final int row;
    public final int column;

    public Position(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public Position(int row, Character column) {
        if (column < 'a' || column > 'h') {
            throw new IllegalArgumentException("Column out of range");
        }
        this.column = column - 'a';
        this.row = row;
    }

    public Position(String position) {
        if (position.length() != 2 || position.charAt(0) < 'a' || position.charAt(0) > 'h') {
            throw new IllegalArgumentException("Invalid Position");
        }

        this.column = position.charAt(0) - 'a';
        this.row = Integer.parseInt(position, 1, 2, 10);
    }

    public String getNotation() {
        char columnChar = (char) ('a' + column);
        char rowChar = (char) ('1' + row);

        return Character.toString(columnChar) + rowChar;
    }

    // TODO: refactor?
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
