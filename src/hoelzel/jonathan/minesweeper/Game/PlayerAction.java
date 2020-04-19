package hoelzel.jonathan.minesweeper.Game;

import java.util.Objects;

public class PlayerAction {
    public enum ActionType {
        FLAG, CLICK
    }

    private final ActionType type;
    private final Coord coord;

    public PlayerAction(ActionType aType, Coord aCoord) {
        type = aType;
        coord = aCoord;
    }

    public ActionType getType() {
        return type;
    }

    public Coord getCoord() {
        return coord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerAction that = (PlayerAction) o;
        return type == that.type &&
                Objects.equals(coord, that.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, coord);
    }
}
