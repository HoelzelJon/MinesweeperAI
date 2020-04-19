package hoelzel.jonathan.minesweeper.Game;

public class InvalidAccessException extends RuntimeException {
    public InvalidAccessException() {
        super("Attempted to access cell's contents before clicking it");
    }
}
