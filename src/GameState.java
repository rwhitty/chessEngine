public class GameState {
    public boolean whiteTurn;
    public boolean whiteKingSide;
    public boolean blackKingSide;
    public boolean whiteQueenSide;
    public boolean blackQueenSide;

    public GameState() {
        this.whiteTurn = true;
        this.whiteKingSide = true;
        this.blackKingSide = true;
        this.whiteQueenSide = true;
        this.blackQueenSide = true;
    }

    public void takeTurn() {
        this.whiteTurn = !this.whiteTurn;
    }
}
