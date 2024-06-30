package board.pieces;

import board.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(int file, int rank, boolean isWhite) {
        super("pawn", file, rank, 1, isWhite);
    }

    public List<Move> getMoves(Board board) {
        List<Move> movesList = new ArrayList<>();

        if (this.isWhite) {
            // We're going up in rank
            if (this.rank < 7 && board.getPiece(this.file, this.rank + 1) == null) {
                // Forward move
                movesList.add(new Move(board, this, this.file, this.rank + 1));
                if (board.getPiece(this.file, this.rank + 2) == null && this.rank == 1) {
                    // Doubly-forward move
                    movesList.add(new Move(board, this, this.file, this.rank + 2));
                }
            }

            if (this.file > 0) {
                Piece currPiece = board.getPiece(this.file - 1, this.rank + 1);
                if (currPiece != null && currPiece.isWhite != this.isWhite) {
                    // We can take diagonally-left
                    movesList.add(new Move(board, this, this.file - 1, this.rank + 1));
                }
            }

            if (this.file < 7) {
                Piece currPiece = board.getPiece(this.file + 1, this.rank + 1);
                if (currPiece != null && currPiece.isWhite != this.isWhite) {
                    // We can take diagonally-right
                    movesList.add(new Move(board, this, this.file + 1, this.rank + 1));
                }
            }
        } else {
            // We're going down in rank
            if (this.rank > 0 && board.getPiece(this.file, this.rank - 1) == null) {
                // Forward move
                movesList.add(new Move(board, this, this.file, this.rank - 1));
                if (board.getPiece(this.file, this.rank - 2) == null && this.rank == 6) {
                    // Doubly-forward move
                    movesList.add(new Move(board, this, this.file, this.rank - 2));
                }
            }

            if (this.file > 0) {
                Piece currPiece = board.getPiece(this.file - 1, this.rank - 1);
                if (currPiece != null && currPiece.isWhite != this.isWhite) {
                    // We can take diagonally-left
                    movesList.add(new Move(board, this, this.file - 1, this.rank - 1));
                }
            }

            if (this.file < 7) {
                Piece currPiece = board.getPiece(this.file + 1, this.rank - 1);
                if (currPiece != null && currPiece.isWhite != this.isWhite) {
                    // We can take diagonally-right
                    movesList.add(new Move(board, this, this.file + 1, this.rank - 1));
                }
            }
        }

        // TODO: Logic for en-passant and promotion
        return movesList;
    }
}
