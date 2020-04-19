package hoelzel.jonathan.minesweeper.Game;

public class GameSetup {
    private int width;
    private int height;
    private int numMines;

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
