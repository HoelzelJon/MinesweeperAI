package hoelzel.jonathan.minesweeper.Game;

import java.util.Iterator;
import java.util.Objects;

import static hoelzel.jonathan.minesweeper.Game.CoordFilter.NO_FILTER;

public class Coord {
    private final int x;
    private final int y;

    public Coord(int aX, int aY) {
        x = aX;
        y = aY;
    }

    public static Iterable<Coord> coordsTo(int xMax, int yMax) {
        return coordsTo(xMax, yMax, NO_FILTER);
    }

    public static Iterable<Coord> coordsTo(int xMax, int yMax, CoordFilter filter) {
        return coordsTo(0, xMax, 0, yMax, filter);
    }

    public static Iterable<Coord> coordsTo(int xMin, int xMax, int yMin, int yMax) {
        return coordsTo(xMin, xMax, yMin, yMax, NO_FILTER);
    }

    public static Iterable<Coord> coordsTo(int xMin, int xMax, int yMin, int yMax, CoordFilter filter) {
        return () -> new CoordIterator(xMin, xMax, yMin, yMax, filter);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x &&
                y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    private static class CoordIterator implements Iterator<Coord> {
        private int minX;
        private int maxX;
        private int maxY;
        private CoordFilter filter;
        private int x;
        private int y;

        public CoordIterator(int aMinX, int aMaxX, int aMinY, int aMaxY, CoordFilter aFilter) {
            minX = aMinX;
            maxX = aMaxX;
            maxY = aMaxY;
            filter = aFilter;

            x = aMinX;
            y = aMinY;
            if (!aFilter.shouldAllow(x, y)) {
                setNextPosition();
            }
        }

        @Override
        public boolean hasNext() {
            return (y < maxY);
        }

        private void setNextPosition() {
            do {
                x++;
                if (x == maxX) {
                    x = minX;
                    y++;
                }
            } while (y < maxY && !filter.shouldAllow(x, y));
        }

        @Override
        public Coord next() {
            if (!hasNext()) {
                return null;
            }
            Coord ret = new Coord(x, y);
            setNextPosition();
            return ret;
        }
    }
}
