package hoelzel.jonathan.minesweeper.Game;

import java.util.ArrayList;

import static hoelzel.jonathan.minesweeper.Game.Board.generateBoard;
import static hoelzel.jonathan.minesweeper.Game.CellStatus.*;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.*;

public class Game {
    private Board board;
    private final int boardWidth;
    private final int boardHeight;
    private final int numMines;
    private int numFlags;
    private boolean clickedMine;
    private int numUnclickedCells;

    public Game(GameSetup setup) {
        boardWidth = setup.getWidth();
        boardHeight = setup.getHeight();
        numMines = setup.getNumMines();
        board = null;
        numUnclickedCells = boardWidth * boardHeight;
        clickedMine = false;
        numFlags = 0;
    }

    public void doAction(PlayerAction action) throws InvalidActionException {
        if (board == null) {
            if (action.getType() == FLAG) {
                throw new InvalidActionException("First action must be a click, not a flag");
            }

            board = generateBoard(boardWidth, boardHeight, numMines, action.getCoord());
        }

        if (action.getType() == CLICK) {
            click(action.getCoord());
        } else if (action.getType() == FLAG) {
            flag(action.getCoord());
        }
    }

    public Board getBoard() {
        if (board == null) {
            return generateBoard(boardWidth, boardHeight, numMines, new Coord(0, 0));
        }
        return board;
    }

    public boolean finished() {
        return numUnclickedCells == numMines || clickedMine;
    }

    public boolean clickedMine() {
        return clickedMine;
    }

    public int getMinesRemaining() {
        return numMines - numFlags;
    }

    public String prettyStr() {
        return "Mines Remaining: " + getMinesRemaining() + "\n"
                + board.prettyStr();
    }

    private void flag(Coord c) throws InvalidActionException {
        if (!board.inBounds(c)) {
            throw new InvalidActionException("Attempting to flag out-of-bounds coord");
        }

        boolean flaggedBefore = board.cellAt(c).getStatus() == FLAGGED;
        board.cellAt(c).flag();
        boolean flaggedAfter = board.cellAt(c).getStatus() == FLAGGED;

        if (flaggedAfter && !flaggedBefore) {
            numFlags ++;
        } else if (flaggedBefore && !flaggedAfter) {
            numFlags --;
        }
    }

    private void click(Coord c) throws InvalidActionException {
        click(c, true);
    }

    private void click(Coord c, boolean recursive) throws InvalidActionException {
        if (!board.inBounds(c)) {
            throw new InvalidActionException("Attempting to click on out-of-bounds coord");
        }

        boolean revealedBefore = board.cellAt(c).getStatus() == REVEALED;
        board.cellAt(c).click();
        boolean revealedAfter = board.cellAt(c).getStatus() == REVEALED;

        if (revealedAfter && board.cellAt(c).hasMine()) {
            clickedMine = true;
        } else if (revealedAfter) {
            if (!revealedBefore) {
                numUnclickedCells --;
            }

            boolean shouldClickAllAdjacent = board.cellAt(c).getNeighborsVal() == 0;
            if (!shouldClickAllAdjacent && recursive && revealedBefore) { // find shouldClickAllAdjacent for revealed-click case
                ArrayList<Coord> flaggedNeighbors = new ArrayList<>(8);
                board.getNeighbors(c, (x,y) -> board.cellAt(x,y).getStatus() == FLAGGED).forEach(flaggedNeighbors::add);
                shouldClickAllAdjacent = flaggedNeighbors.size() == board.cellAt(c).getNeighborsVal();
            }

            if (shouldClickAllAdjacent) {
                for (Coord n : board.getNeighbors(c, (x, y) -> board.cellAt(x, y).getStatus() == UNCLICKED)) {
                    click(n, false);
                }
            }
        }
    }
}
