package hoelzel.jonathan.minesweeper.Player;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Coord;
import hoelzel.jonathan.minesweeper.Game.PlayerAction;

import java.util.Collection;
import java.util.HashSet;

import static hoelzel.jonathan.minesweeper.Game.CellStatus.*;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.CLICK;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.FLAG;
import static hoelzel.jonathan.minesweeper.Util.countIterable;
import static hoelzel.jonathan.minesweeper.Util.toList;

public class AIPlayer1 extends AIPlayer {
    private Player backup;

    public AIPlayer1(PrintMode doPrintouts, Player aBackup) {
        super(doPrintouts);
        backup = aBackup;
    }

    @Override
    protected Collection<PlayerAction> getKnownGoodActions(Board b, int minesRemaining) {
        Collection<PlayerAction> ret = new HashSet<>();
        Iterable<Coord> revealedCoords = b.getAllCoords((x, y) -> b.cellAt(x,y).getStatus() == REVEALED && b.cellAt(x,y).getNeighborsVal() > 0);

        for (Coord c : revealedCoords) {
            Collection<Coord> unclicked = toList(b.getNeighbors(c, (x, y) -> b.cellAt(x, y).getStatus() == UNCLICKED));
            int flaggedNeighbors = countIterable(b.getNeighbors(c, (x, y) -> b.cellAt(x, y).getStatus() == FLAGGED));
            int val = b.cellAt(c).getNeighborsVal();

            if (flaggedNeighbors == val && !unclicked.isEmpty()) {
                ret.add(new PlayerAction(CLICK, c));
            } else if (val - flaggedNeighbors == unclicked.size() && !unclicked.isEmpty()) {
                ret.add(new PlayerAction(FLAG, unclicked.stream().findFirst().get()));
            }
        }

        return ret;
    }

    protected PlayerAction getProbableGoodAction(Board b, int minesRemainig) {
        return backup.getAction(b, minesRemainig);
    }
}
