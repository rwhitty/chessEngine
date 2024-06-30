package board.pieces;

import board.*;

import java.util.List;
public abstract class Piece {
    public String name;
    public int file;
    public int rank;
    public double value;
    public boolean isWhite;
    public Piece(String name, int file, int rank, double value, boolean isWhite) {
        this.name = name;
        this.file = file;
        this.rank = rank;
        this.value = value;
        this.isWhite = isWhite;
    }

    public abstract List<Move> getMoves(Board board);
}
