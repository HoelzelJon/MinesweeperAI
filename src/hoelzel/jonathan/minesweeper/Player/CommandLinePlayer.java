package hoelzel.jonathan.minesweeper.Player;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Coord;
import hoelzel.jonathan.minesweeper.Game.PlayerAction;

import java.util.Map;
import java.util.TreeMap;

import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.CLICK;
import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.FLAG;
import static hoelzel.jonathan.minesweeper.InputUtil.*;

public class CommandLinePlayer implements Player {
    private static Map<String, PlayerAction.ActionType> actionMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        actionMap.put("C", CLICK);
        actionMap.put("F", FLAG);
    }

    private Coord getCoord() {
        int x = getInt("X");
        int y = getInt("Y");
        return new Coord(x, y);
    }

    @Override
    public PlayerAction getAction(Board b, int minesRemaining) {
        System.out.println("Mines Remaining: " + minesRemaining);
        System.out.println(b.prettyStr());

        if (getBool("Do Random Click")) {
            return RANDOM_CLICKER.getAction(b, minesRemaining);
        } else {
            return new PlayerAction(getChoice("Action Type", actionMap), getCoord());
        }
    }
}
