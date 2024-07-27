public class GameState implements Cloneable {
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

    @Override
    public GameState clone() {
        GameState newState = new GameState();
        newState.whiteTurn = this.whiteTurn;
        newState.whiteKingSide = this.whiteKingSide;
        newState.blackKingSide = this.blackKingSide;
        newState.whiteQueenSide = this.whiteQueenSide;
        newState.blackQueenSide = this.blackQueenSide;
        return newState;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GameState)) {
            return false;
        }
        GameState otherState = (GameState) obj;
        return (this.whiteTurn == otherState.whiteTurn) &&
                (this.whiteKingSide == otherState.whiteKingSide) &&
                (this.blackKingSide == otherState.blackKingSide) &&
                (this.whiteQueenSide == otherState.whiteQueenSide) &&
                (this.blackQueenSide == otherState.blackQueenSide);
    }
}
