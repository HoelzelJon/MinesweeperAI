package hoelzel.jonathan.minesweeper.Game;

public interface CoordFilter {
    CoordFilter NO_FILTER = (x, y) -> true;

    boolean shouldAllow(int x, int y);
}
