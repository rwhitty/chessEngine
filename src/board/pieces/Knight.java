package board.pieces;

import board.*;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(int file, int rank, boolean isWhite) {
        super("knight", file, rank, 3.05, isWhite);
    }

    public List<Move> getMoves(Board board) {
        List<Move> movesList = new ArrayList<>();

        if (this.file < 6 && this.rank < 7) {
            Piece currPiece = board.getPiece(this.file + 2, this.rank + 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file + 2, this.rank + 1));
            }
        }

        if (this.file < 7 && this.rank < 6) {
            Piece currPiece = board.getPiece(this.file + 1, this.rank + 2);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file + 1, this.rank + 2));
            }
        }

        if (this.file > 0 && this.rank < 6) {
            Piece currPiece = board.getPiece(this.file - 1, this.rank + 2);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file - 1, this.rank + 2));
            }
        }

        if (this.file > 1 && this.rank < 7) {
            Piece currPiece = board.getPiece(this.file - 2, this.rank + 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file - 2, this.rank + 1));
            }
        }

        if (this.file > 1 && this.rank > 0) {
            Piece currPiece = board.getPiece(this.file - 2, this.rank - 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file - 2, this.rank - 1));
            }
        }

        if (this.file > 0 && this.rank > 1) {
            Piece currPiece = board.getPiece(this.file - 1, this.rank - 2);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file - 1, this.rank - 2));
            }
        }

        if (this.file < 7 && this.rank > 1) {
            Piece currPiece = board.getPiece(this.file + 1, this.rank - 2);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file + 1, this.rank - 2));
            }
        }

        if (this.file < 6 && this.rank > 0) {
            Piece currPiece = board.getPiece(this.file + 2, this.rank - 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file + 2, this.rank - 1));
            }
        }

        return movesList;
    }
}
