package hoelzel.jonathan.minesweeper.Game;

import static hoelzel.jonathan.minesweeper.Game.CellStatus.*;

public class Cell {
    private boolean hasMine;
    private int neighborsVal;
    private CellStatus status;

    public Cell(boolean mine, int neighbors) {
        hasMine = mine;
        neighborsVal = neighbors;
        status = UNCLICKED;
    }

    boolean hasMine() throws InvalidAccessException {
        if (status != REVEALED) {
            throw new InvalidAccessException();
        }

        return hasMine;
    }

    public CellStatus getStatus() {
        return status;
    }

    void click() {
        if (status != FLAGGED) {
            this.status = REVEALED;
        }
    }

    void flag() {
        if (status == FLAGGED) {
            this.status = UNCLICKED;
        } else if (status != REVEALED) {
            this.status = FLAGGED;
        }
    }

    public int getNeighborsVal() throws InvalidAccessException {
        if (status != REVEALED) {
            throw new InvalidAccessException();
        }
        return neighborsVal;
    }

    public String prettyStr() {
        switch (status) {
            case UNCLICKED:
                return ".";
            case FLAGGED:
                return "|";//"\u2691";
            case REVEALED:
                if (hasMine) {
                    return "\u2605";
                } else if (neighborsVal == 0) {
                    return " ";
                } else {
                    return "" + neighborsVal;
                }
        }
        return "ERROR";
    }
}
