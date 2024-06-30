package board.pieces;

import board.*;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(int file, int rank, boolean isWhite) {
        super("king", file, rank, 1000, isWhite);
    }

    public List<Move> getMoves(Board board) {
        List<Move> movesList = new ArrayList<>();

        if (this.file < 7) {
            Piece currPiece = board.getPiece(this.file + 1, this.rank);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file + 1, this.rank));
            }
        }

        if (this.file < 7 && this.rank < 7) {
            Piece currPiece = board.getPiece(this.file + 1, this.rank + 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file + 1, this.rank + 1));
            }
        }

        if (this.rank < 7) {
            Piece currPiece = board.getPiece(this.file, this.rank + 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file, this.rank + 1));
            }
        }

        if (this.file > 0 && this.rank < 7) {
            Piece currPiece = board.getPiece(this.file - 1, this.rank + 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file - 1, this.rank + 1));
            }
        }

        if (this.file > 0) {
            Piece currPiece = board.getPiece(this.file - 1, this.rank);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file - 1, this.rank));
            }
        }

        if (this.file > 0 && this.rank > 0) {
            Piece currPiece = board.getPiece(this.file - 1, this.rank - 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file - 1, this.rank - 1));
            }
        }

        if (this.rank > 0) {
            Piece currPiece = board.getPiece(this.file, this.rank - 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file, this.rank - 1));
            }
        }

        if (this.file < 7 && this.rank > 0) {
            Piece currPiece = board.getPiece(this.file + 1, this.rank - 1);
            if (currPiece == null || currPiece.isWhite != this.isWhite) {
                movesList.add(new Move(board, this, this.file + 1, this.rank - 1));
            }
        }

        // TODO: Need logic for avoiding check in here. Can we simply avoid checks by giving king value 1000000?
        return movesList;
    }
}
