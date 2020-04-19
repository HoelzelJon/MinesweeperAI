package hoelzel.jonathan.minesweeper.Game;

public class GameSetup {
    private final int width;
    private final int height;
    private final int numMines;

    public GameSetup(int aWidth, int aHeight, int aNumMines) {
        width = aWidth;
        height = aHeight;
        numMines = aNumMines;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumMines() {
        return numMines;
    }
}
