package hoelzel.jonathan.minesweeper.Game;

import java.util.*;

import static hoelzel.jonathan.minesweeper.Game.Coord.coordsTo;
import static hoelzel.jonathan.minesweeper.Game.CoordFilter.NO_FILTER;
import static hoelzel.jonathan.minesweeper.Util.countIterable;
import static hoelzel.jonathan.minesweeper.Util.toList;

public class Board {
    private final Cell[][] cells; // indexed [x][y]
    private final int width;
    private final int height;

    static Board generateBoard(int width, int height, int nMines, Coord initialClick) {
        CoordFilter avoidInitialClick = (x, y) -> Math.abs(x - initialClick.x()) > 1 || Math.abs(y - initialClick.y()) > 1;
        List<Coord> allCoords = toList(coordsTo(width, height, avoidInitialClick));
        Collections.shuffle(allCoords);
        Collection<Coord> coordsWithMines = new HashSet<>(allCoords.subList(0, nMines));
        return new Board(width, height, coordsWithMines);
    }

    public Board(Cell[][] cells) {
        this.cells = cells;
        this.width = cells.length;
        this.height = cells[0].length;
    }

    public String prettyStr() {
        StringBuilder ret = new StringBuilder();
        for (int y = height - 1; y >= 0; y --) {
            for (int x = 0; x < width; x ++) {
                ret.append(cells[x][y].prettyStr());
                ret.append("  ");
            }
            ret.append('\n');
        }
        return ret.toString();
    }

    private Board(int aWidth, int aHeight, Collection<Coord> minePositions) {
        width = aWidth;
        height = aHeight;

        cells = new Cell[width][height];

        for (Coord c : coordsTo(width, height)) {
            boolean hasMine = minePositions.contains(c);
            int mineNeighbors = countIterable(getNeighbors(c, (x, y) -> minePositions.contains(new Coord(x, y))));

            cells[c.x()][c.y()] = new Cell(hasMine, mineNeighbors);
        }
    }

    public boolean inBounds(Coord c) {
        return inBounds(c.x(), c.y());
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Iterable<Coord> getNeighbors(Coord c) {
        return getNeighbors(c, NO_FILTER);
    }

    public Iterable<Coord> getAllCoords() {
        return getAllCoords(NO_FILTER);
    }

    public Iterable<Coord> getAllCoords(CoordFilter filter) {
        return coordsTo(width, height, filter);
    }

    public Iterable<Coord> getNeighbors(Coord c, CoordFilter filter) {
        return coordsTo(c.x() - 1,  c.x() + 2, c.y() - 1, c.y() + 2,
                (x, y) -> (x != c.x() || y != c.y()) && inBounds(x,y) && filter.shouldAllow(x, y));
    }

    public Cell cellAt(Coord c) {
        return cellAt(c.x(), c.y());
    }

    public Cell cellAt(int x, int y) {
        return cells[x][y];
    }
}
