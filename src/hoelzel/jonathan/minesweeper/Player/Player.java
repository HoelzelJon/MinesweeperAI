package hoelzel.jonathan.minesweeper.Player;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Coord;
import hoelzel.jonathan.minesweeper.Game.PlayerAction;
import hoelzel.jonathan.minesweeper.Util;

import java.util.List;

import static hoelzel.jonathan.minesweeper.Game.CellStatus.UNCLICKED;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.CLICK;

public interface Player {
    Player RANDOM_CLICKER = (b, m) -> {
        List<Coord> clickable = Util.toList(b.getAllCoords((x, y) -> b.cellAt(x,y).getStatus() == UNCLICKED));
        return new PlayerAction(CLICK, clickable.get((int)(Math.random() * clickable.size())));
    };

    PlayerAction getAction(Board b, int minesRemaining);
}
