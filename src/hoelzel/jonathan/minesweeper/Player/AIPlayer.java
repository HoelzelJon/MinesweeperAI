package hoelzel.jonathan.minesweeper.Player;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Coord;
import hoelzel.jonathan.minesweeper.Game.PlayerAction;

import java.util.*;

import static hoelzel.jonathan.minesweeper.Game.CellStatus.REVEALED;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.CLICK;
import static hoelzel.jonathan.minesweeper.Util.countIterable;

public abstract class AIPlayer implements Player {
    public enum PrintMode {
        NO_PRINT, PRINT_EACH_STEP, PRINT_WHEN_THINKING
    }

    private PrintMode print;
    private Collection<PlayerAction> queuedActions;

    protected AIPlayer(PrintMode aPrint) {
        print = aPrint;
        queuedActions = new HashSet<>();
    }

    protected abstract Collection<PlayerAction> getKnownGoodActions(Board b, int minesRemaining);

    protected abstract PlayerAction getProbableGoodAction(Board b, int minesRemainig);

    private void print(Board b, int minesRemaining) {
        System.out.println("Mines Remaining: " + minesRemaining);
        System.out.println(b.prettyStr());
    }

    @Override
    public PlayerAction getAction(Board b, int minesRemaining) {
        if (print == PrintMode.PRINT_EACH_STEP) {
            print(b, minesRemaining);
        }

        if (queuedActions.isEmpty()) {
            if (print ==  PrintMode.PRINT_WHEN_THINKING) {
                print(b, minesRemaining);
            }
            queuedActions = getKnownGoodActions(b, minesRemaining);
        }

        if (!queuedActions.isEmpty()) {
            PlayerAction action = queuedActions.stream().findAny().get();
            queuedActions.remove(action);
            return action;
        }

        Iterable<Coord> revealedCoords = b.getAllCoords((x, y) -> b.cellAt(x,y).getStatus() == REVEALED && b.cellAt(x,y).getNeighborsVal() > 0);

        if (countIterable(revealedCoords) == 0) {
            //List<Coord> allCoords = toList(b.getAllCoords());
            //return new PlayerAction(CLICK, allCoords.get((int)(Math.random() * allCoords.size())));
            return new PlayerAction(CLICK, new Coord(2, 2));
        } else {
            return getProbableGoodAction(b, minesRemaining);
        }
    }
}
