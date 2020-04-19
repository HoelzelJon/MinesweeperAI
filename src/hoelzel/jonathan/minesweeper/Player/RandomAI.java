package hoelzel.jonathan.minesweeper.Player;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Coord;
import hoelzel.jonathan.minesweeper.Game.PlayerAction;

import java.util.*;

import static hoelzel.jonathan.minesweeper.Game.PlayerAction.ActionType.CLICK;
import static hoelzel.jonathan.minesweeper.Player.Player.RANDOM_CLICKER;

public interface RandomAI {
    PlayerAction getAction(Board b, int minesRemaining, Map<Set<Coord>, AIPlayer3.MineRange> minesPerUnclickedGroup);

    RandomAI UNIFORM_RANDOM = (b, m, map) -> RANDOM_CLICKER.getAction(b, m);

    RandomAI HUMAN_CHOICE = (b, m, map) -> new CommandLinePlayer().getAction(b, m);

    // find the group with the lowest probability (maxMines + minMines) / unclicked
    // and select a random cell from that group
    RandomAI LOWEST_PROB_GROUP = (b, minesRemaining, minesPerUnclickedGroup) -> {
        double minFraction = Double.POSITIVE_INFINITY;
        Set<Coord> minGroup = null;

        for (Map.Entry<Set<Coord>, AIPlayer3.MineRange> entry : minesPerUnclickedGroup.entrySet()) {
            double numSpaces = entry.getKey().size();
            double numMines = entry.getValue().max + entry.getValue().min;
            double fraction = numMines / numSpaces;
            if (fraction < minFraction) {
                minFraction = fraction;
                minGroup = entry.getKey();
            }
        }

        if (minGroup == null) {
            return UNIFORM_RANDOM.getAction(b, minesRemaining, minesPerUnclickedGroup);
        } else {
            return new PlayerAction(CLICK, minGroup.stream().findAny().get());
        }
    };

    // calculate group probabilities as above, but take the average probability of all groups for each coordinate
    // and then choose the coordinate with the lowest value
    RandomAI LOWEST_AVG_PROB_COORD = (b, minesRemaining, minesPerUnclickedGroup) -> {
        Map<Coord, Collection<Double>> probsByCoord = new HashMap<>();

        for (Map.Entry<Set<Coord>, AIPlayer3.MineRange> entry : minesPerUnclickedGroup.entrySet()) {
            double numSpaces = entry.getKey().size();
            double numMines = entry.getValue().max + entry.getValue().min;
            entry.getKey().forEach(e -> probsByCoord.computeIfAbsent(e, e1 -> new HashSet<>()).add(numMines / numSpaces));
        }

        double minAvg = Double.POSITIVE_INFINITY;
        Coord minCoord = null;

        for (Map.Entry<Coord, Collection<Double>> entry : probsByCoord.entrySet()) {
            double average = entry.getValue().stream().mapToDouble(a -> a).average().orElse(Double.POSITIVE_INFINITY);
            if (average < minAvg) {
                minAvg = average;
                minCoord = entry.getKey();
            }
        }

        if (minCoord == null) {
            return UNIFORM_RANDOM.getAction(b, minesRemaining, minesPerUnclickedGroup);
        } else {
            return new PlayerAction(CLICK, minCoord);
        }
    };
}
