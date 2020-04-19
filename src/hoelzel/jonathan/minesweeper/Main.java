package hoelzel.jonathan.minesweeper;

import hoelzel.jonathan.minesweeper.Game.*;
import hoelzel.jonathan.minesweeper.Player.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import static hoelzel.jonathan.minesweeper.InputUtil.*;
import static hoelzel.jonathan.minesweeper.Player.AIPlayer.PrintMode.*;
import static hoelzel.jonathan.minesweeper.Player.RandomAI.*;

public class Main {
    // TODO: figure out why this slows it down a lot without the limit on minesRemaining and fix it
    private static final int MAX_UNCLICKED_CELLS_FOR_CHECKING = 10;

    public static void main(String[] args) {
        // TODO: level 4: add probability for when bot doesn't know what to do
        // TODO: run some stuff to find out where the optimal starting click is

        Map<String, GameSetup> difficultyMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        difficultyMap.put("EASY", new GameSetup(9, 9, 10));
        difficultyMap.put("MED", new GameSetup(16, 16, 40));
        difficultyMap.put("HARD", new GameSetup(30, 16, 99));
        difficultyMap.put("EXTREME", new GameSetup(30, 24, 180));
        GameSetup setup = getChoice("Board Difficulty", difficultyMap);

        Map<String, AIPlayer.PrintMode> printModeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        printModeMap.put("ALWAYS", PRINT_EACH_STEP);
        printModeMap.put("THINK", PRINT_WHEN_THINKING);
        printModeMap.put("NONE", NO_PRINT);
        AIPlayer.PrintMode printout = getChoice("Print Mode", printModeMap);

        Map<String, Supplier<RandomAI>> backupPlayerMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        backupPlayerMap.put("UNIFORM", () -> UNIFORM_RANDOM);
        backupPlayerMap.put("HUMAN", () -> HUMAN_CHOICE);
        backupPlayerMap.put("GROUP", () -> LOWEST_PROB_GROUP);
        backupPlayerMap.put("AVERAGE", () -> LOWEST_AVG_PROB_COORD);
        Supplier<RandomAI> backupFactory = getChoice("Backup Player", backupPlayerMap);

        Supplier<Player> playerFactory = () -> new AIPlayer3(printout, MAX_UNCLICKED_CELLS_FOR_CHECKING, backupFactory.get());
        int delay = getInt("Delay");
        int iters = getInt("Iterations");

        int successes = 0;
        int failures = 0;

        for (int i = 0; i < iters; i ++) {
            if (i % 100 == 0) {
                System.out.println("Starting game " + i);
            }

            Game g = new Game(setup);
            Player p = playerFactory.get();

            while (!g.finished()) {
                long startTime = System.currentTimeMillis();

                try {
                    g.doAction(p.getAction(g.getBoard(), g.getMinesRemaining()));
                } catch (InvalidActionException ex) {
                    ex.printStackTrace();
                }
                long duration = System.currentTimeMillis() - startTime;
                if (delay > duration) {
                    try {
                        Thread.sleep(delay - duration);
                    } catch (InterruptedException ex) {
                        // do nothing
                    }
                }
            }

            if (g.clickedMine()) {
                failures ++;
            } else {
                successes ++;
            }
        }

        System.out.println("Successes: " + successes);
        System.out.println("Failures: " + failures);
    }
}
