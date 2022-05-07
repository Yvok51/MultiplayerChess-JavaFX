package multiplayerchess.multiplayerchess.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * A position on the board.
 */
public final class Position implements Serializable {
    static final long serialVersionUID = 0x12345;
    // public static final Position InvalidPosition = new Position(-1, -1);

    public final int row;
    public final int column;

    /**
     * Position constructor from the zero indexed row and column.
     *
     * @param row    The row of the position.
     * @param column The column of the position.
     */
    public Position(int row, int column) {
        this.column = column;
        this.row = row;
    }

    /**
     * Position constructor from parsed chess notation.
     *
     * @param row    The row of the position.
     * @param column The column of the position in chess notation.
     */
    public Position(int row, Character column) {
        column = Character.toLowerCase(column);
        if (column < 'a' || column > 'h') {
            throw new IllegalArgumentException("Column out of range");
        }
        this.column = column - 'a';
        this.row = row;
    }

    /**
     * Position constructor from the chess notation.
     *
     * @param position The position in chess notation.
     */
    public Position(String position) {
        if (position.length() != 2 || position.charAt(0) < 'a' || position.charAt(0) > 'h') {
            throw new IllegalArgumentException("Invalid Position");
        }

        this.column = position.charAt(0) - 'a';
        this.row = Integer.parseInt(position, 1, 2, 10) - 1;
    }

    /**
     * Gets this position in chess notation.
     *
     * @return The position in chess notation.
     */
    public String getNotation() {
        char columnChar = (char) ('a' + column);
        char rowChar = (char) ('1' + row);

        return Character.toString(columnChar) + rowChar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
