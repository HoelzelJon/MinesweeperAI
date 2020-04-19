package hoelzel.jonathan.minesweeper.Player;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Coord;
import hoelzel.jonathan.minesweeper.Game.PlayerAction;

import java.util.*;

import static hoelzel.jonathan.minesweeper.Game.CellStatus.*;
import static hoelzel.jonathan.minesweeper.Game.CellStatus.UNCLICKED;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.CLICK;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.FLAG;
import static hoelzel.jonathan.minesweeper.Util.*;

public class AIPlayer3 extends AIPlayer {
    private RandomAI backup;
    private int maxUnclickedCellsForChecking;
    private Map<Set<Coord>, MineRange> minesPerUnclickedGroup;

    public AIPlayer3(PrintMode doPrintouts, int aMaxUnclickedCellsForChecking, RandomAI aBackup) {
        super(doPrintouts);
        backup = aBackup;
        maxUnclickedCellsForChecking = aMaxUnclickedCellsForChecking;
    }

    static class MineRange {
        int min;
        int max;

        private MineRange(int aMin, int aMax) {
            min = aMin;
            max = aMax;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MineRange mineRange = (MineRange) o;
            return min == mineRange.min &&
                    max == mineRange.max;
        }

        @Override
        public int hashCode() {
            return Objects.hash(min, max);
        }

        public boolean moreConstrained(MineRange other) {
            return (min > other.min) || (max < other.max);
        }
    }

    @Override
    protected Collection<PlayerAction> getKnownGoodActions(Board b, int minesRemaining) {
        Iterable<Coord> revealedCoords = b.getAllCoords((x, y) -> b.cellAt(x, y).getStatus() == REVEALED && b.cellAt(x, y).getNeighborsVal() > 0);

        minesPerUnclickedGroup = new HashMap<>();

        for (Coord c : revealedCoords) {
            Set<Coord> unclicked = toSet(b.getNeighbors(c, (x, y) -> b.cellAt(x, y).getStatus() == UNCLICKED));
            int flaggedNeighbors = countIterable(b.getNeighbors(c, (x, y) -> b.cellAt(x, y).getStatus() == FLAGGED));
            int val = b.cellAt(c).getNeighborsVal();

            if (!unclicked.isEmpty()) {
                int netMines = val - flaggedNeighbors;
                minesPerUnclickedGroup.put(unclicked, new MineRange(netMines, netMines));
            }
        }

        if (countIterable(b.getAllCoords((x,y) -> b.cellAt(x,y).getStatus() == UNCLICKED)) < maxUnclickedCellsForChecking) {
            Set<Coord> allUnclicked = toSet(b.getAllCoords((x, y) -> b.cellAt(x, y).getStatus() == UNCLICKED));
            minesPerUnclickedGroup.put(allUnclicked, new MineRange(minesRemaining, minesRemaining));
        }

        Collection<PlayerAction> ret = new HashSet<>();

        boolean shouldContinue;
        do {
            shouldContinue = false;

            List<Set<Coord>> groupList = new ArrayList<>(minesPerUnclickedGroup.size());
            List<MineRange> rangeList = new ArrayList<>(minesPerUnclickedGroup.size());

            for (Map.Entry<Set<Coord>, MineRange> entry : minesPerUnclickedGroup.entrySet()) {
                groupList.add(entry.getKey());
                rangeList.add(entry.getValue());
            }

            for (int thisIdx = 0; thisIdx < groupList.size(); thisIdx ++) {
                Set<Coord> thisGroup = groupList.get(thisIdx);
                MineRange thisRange = rangeList.get(thisIdx);

                if (thisRange.max == 0) {
                    thisGroup.forEach(c -> ret.add(new PlayerAction(CLICK, c)));
                } else if (thisGroup.size() == thisRange.min) {
                    thisGroup.forEach(c -> ret.add(new PlayerAction(FLAG, c)));
                }

                for (int otherIdx = thisIdx + 1; otherIdx < groupList.size(); otherIdx ++) {
                    Set<Coord> otherGroup = groupList.get(otherIdx);
                    MineRange otherRange = rangeList.get(otherIdx);

                    if (!intersects(thisGroup, otherGroup)) {
                        continue;
                    }

                    Set<Coord> thisMinusOther = minus(thisGroup, otherGroup);
                    Set<Coord> otherMinusThis = minus(otherGroup, thisGroup);
                    Set<Coord> intersection = intersection(thisGroup, otherGroup);

                    MineRange intersectionRange = new MineRange(
                            Math.max(Math.max(0, thisRange.min - thisMinusOther.size()), otherRange.min - otherMinusThis.size()),
                            Math.min(Math.min(intersection.size(), thisRange.max), otherRange.max)
                    );

                    Map<Set<Coord>, MineRange> newRanges = new HashMap<>();
                    newRanges.put(intersection, intersectionRange);

                    newRanges.put(thisMinusOther, new MineRange(
                            Math.max(thisRange.min - intersectionRange.max, 0),
                            Math.min(thisRange.max - intersectionRange.min, thisMinusOther.size())
                    ));

                    newRanges.put(otherMinusThis, new MineRange(
                            Math.max(otherRange.min - intersectionRange.max, 0),
                            Math.min(otherRange.max - intersectionRange.min, otherMinusThis.size())
                    ));

                    for (Map.Entry<Set<Coord>, MineRange> newEntry : newRanges.entrySet()) {
                        if (!newEntry.getKey().isEmpty()) {
                            if (!minesPerUnclickedGroup.containsKey(newEntry.getKey())) {
                                minesPerUnclickedGroup.put(newEntry.getKey(), newEntry.getValue());
                                shouldContinue = true;
                            } else {
                                MineRange existingRange = minesPerUnclickedGroup.get(newEntry.getKey());

                                if (newEntry.getValue().moreConstrained(existingRange)) {
                                    minesPerUnclickedGroup.put(newEntry.getKey(), new MineRange(
                                            Math.max(existingRange.min, newEntry.getValue().min),
                                            Math.min(existingRange.max, newEntry.getValue().max)
                                    ));
                                    shouldContinue = true;
                                }
                            }
                        }
                    }
                }
            }

//            if (!ret.isEmpty()) {
//                shouldContinue = false;
//            }
        } while (shouldContinue);

        return ret;
    }

    protected PlayerAction getProbableGoodAction(Board b, int minesRemaining) {
        return backup.getAction(b, minesRemaining, minesPerUnclickedGroup);
    }
}
