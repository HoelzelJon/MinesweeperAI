package hoelzel.jonathan.minesweeper;

import hoelzel.jonathan.minesweeper.Game.Board;
import hoelzel.jonathan.minesweeper.Game.Cell;
import hoelzel.jonathan.minesweeper.Player.AIPlayer;
import hoelzel.jonathan.minesweeper.Player.AIPlayer3;
import hoelzel.jonathan.minesweeper.Player.RandomAI;

public class CustomBoardMain {
    public static void main(String[] args) {
        Board board = new Board(new Cell[][] {
                {e(0), e(1), f(), m(), m()},
                {e(1), e(2), e(3), m(), b()},
                {f(), e(3), e(2), b(), b()},
                {m(), m(), b(), b(), b(), b()},
                {m(), m(), b(), b(), b(), m()}
        });
        int totalMines = 9;

        AIPlayer player = new AIPlayer3(AIPlayer.PrintMode.PRINT_EACH_STEP, 100, RandomAI.HUMAN_CHOICE);
        System.out.println(player.getAction(board, totalMines));
    }

    // flagged cell
    private static Cell f() {
        Cell c = new Cell(true, 0);
        c.flag();
        return c;
    }

    // empty cell (clicked, with n neighbors)
    private static Cell e(int n) {
        Cell c = new Cell(false, n);
        c.click();
        return c;
    }

    // unclicked cell with mine
    private static Cell m() {
        return new Cell(true, 0);
    }

    // cell without mine
    private static Cell b() {
        return new Cell(false, 0);
    }
}
