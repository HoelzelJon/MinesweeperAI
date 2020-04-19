package hoelzel.jonathan.minesweeper.Player;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Coord;
import hoelzel.jonathan.minesweeper.Game.PlayerAction;

import java.util.*;

import static hoelzel.jonathan.minesweeper.Game.CellStatus.*;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.CLICK;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.FLAG;
import static hoelzel.jonathan.minesweeper.Util.*;

public class AIPlayer2 extends AIPlayer {
    private Player backup;
    private int maxUnclickedCellsForChecking;

    public AIPlayer2(PrintMode doPrintouts, Player aBackup, int aMaxUnclickedCellsForChecking) {
        super(doPrintouts);
        backup = aBackup;
        maxUnclickedCellsForChecking = aMaxUnclickedCellsForChecking;
    }

    @Override
    protected Collection<PlayerAction> getKnownGoodActions(Board b, int minesRemaining) {
        Iterable<Coord> revealedCoords = b.getAllCoords((x, y) -> b.cellAt(x,y).getStatus() == REVEALED && b.cellAt(x,y).getNeighborsVal() > 0);

        Map<Set<Coord>, Integer> minesPerUnclickedGroup = new HashMap<>();

        for (Coord c : revealedCoords) {
            Set<Coord> unclicked = toSet(b.getNeighbors(c, (x, y) -> b.cellAt(x, y).getStatus() == UNCLICKED));
            int flaggedNeighbors = countIterable(b.getNeighbors(c, (x, y) -> b.cellAt(x, y).getStatus() == FLAGGED));
            int val = b.cellAt(c).getNeighborsVal();

            if (!unclicked.isEmpty()) {
                minesPerUnclickedGroup.put(unclicked, val - flaggedNeighbors);
            }
        }

        if (countIterable(b.getAllCoords((x,y) -> b.cellAt(x,y).getStatus() == UNCLICKED)) < maxUnclickedCellsForChecking) {
            Set<Coord> allUnclicked = toSet(b.getAllCoords((x, y) -> b.cellAt(x, y).getStatus() == UNCLICKED));
            minesPerUnclickedGroup.put(allUnclicked, minesRemaining);
        }

        Collection<PlayerAction> ret = new HashSet<>();

        boolean shouldContinue;
        do {
            shouldContinue = false;
            Map<Set<Coord>, Integer> newMinesPerUnclickedGroup = new HashMap<>(minesPerUnclickedGroup);

            for (Set<Coord> mineGroup : minesPerUnclickedGroup.keySet()) {
                if (mineGroup.isEmpty()) {
                    continue;
                }

                if (minesPerUnclickedGroup.get(mineGroup) == 0) {
                    mineGroup.forEach(c -> ret.add(new PlayerAction(CLICK, c)));
                } else if (mineGroup.size() == minesPerUnclickedGroup.get(mineGroup)) {
                    mineGroup.forEach(c -> ret.add(new PlayerAction(FLAG, c)));
                }

                for (Set<Coord> otherGroup : minesPerUnclickedGroup.keySet()) {
                    if (otherGroup.equals(mineGroup)) {
                        continue;
                    }

                    int thisMineNum = minesPerUnclickedGroup.get(mineGroup);
                    int otherMineNum = minesPerUnclickedGroup.get(otherGroup);
                    Set<Coord> mineMinusOther = minus(mineGroup, otherGroup);

                    if (mineGroup.containsAll(otherGroup)) {
                        newMinesPerUnclickedGroup.put(mineMinusOther, thisMineNum - otherMineNum);
                        newMinesPerUnclickedGroup.remove(mineGroup);
                        shouldContinue = true;
                    } else if (mineMinusOther.size() > 0 && mineMinusOther.size() == (thisMineNum - otherMineNum)) {
                        ret.add(new PlayerAction(FLAG, mineMinusOther.stream().findAny().get()));
                    }
                }
            }

            minesPerUnclickedGroup = newMinesPerUnclickedGroup;
        } while (shouldContinue);

        return ret;
    }

    protected PlayerAction getProbableGoodAction(Board b, int minesRemainig) {
        return backup.getAction(b, minesRemainig);
    }
}
