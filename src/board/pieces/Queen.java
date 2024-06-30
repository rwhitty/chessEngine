package board.pieces;

import board.*;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(int file, int rank, boolean isWhite) {
        super("queen", file, rank, 9.5, isWhite);
    }

    public List<Move> getMoves(Board board) {
        List<Move> movesList = new ArrayList<>();

        // First loop, for going straight up
        for (int dist = 1; this.file + dist < 8; dist++) {
            Piece currPiece = board.getPiece(this.file + dist, this.rank);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file + dist, this.rank));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file + dist, this.rank));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        // Second loop, for going diagonally up-right
        for (int dist = 1; this.file + dist < 8 && this.rank + dist < 8; dist++) {
            Piece currPiece = board.getPiece(this.file + dist, this.rank + dist);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file + dist, this.rank + dist));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file + dist, this.rank + dist));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        // Third loop, for going straight right
        for (int dist = 1; this.rank + dist < 8; dist++) {
            Piece currPiece = board.getPiece(this.file, this.rank + dist);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file, this.rank + dist));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file, this.rank + dist));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        // Fourth loop, for going diagonally down-right
        for (int dist = 1; this.file - dist >= 0 && this.rank + dist < 8; dist++) {
            Piece currPiece = board.getPiece(this.file - dist, this.rank + dist);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file - dist, this.rank + dist));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file - dist, this.rank + dist));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        // Fifth loop, for going straight down
        for (int dist = 1; this.file - dist >= 0; dist++) {
            Piece currPiece = board.getPiece(this.file - dist, this.rank);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file - dist, this.rank));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file - dist, this.rank));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        // Sixth loop, for going diagonally, down-left
        for (int dist = 1; this.file - dist >= 0 && this.rank - dist >= 0; dist++) {
            Piece currPiece = board.getPiece(this.file - dist, this.rank - dist);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file - dist, this.rank - dist));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file - dist, this.rank - dist));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        // Seventh loop, for going straight left
        for (int dist = 1; this.rank - dist >= 0; dist++) {
            Piece currPiece = board.getPiece(this.file, this.rank - dist);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file, this.rank - dist));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file, this.rank - dist));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        // Eighth loop, for going diagonally up-left
        for (int dist = 1; this.file + dist < 8 && this.rank - dist >= 0; dist++) {
            Piece currPiece = board.getPiece(this.file + dist, this.rank - dist);
            if (currPiece == null) {
                // Nothing at this square, totally legal
                movesList.add(new Move(board, this, this.file + dist, this.rank - dist));
            } else if (currPiece.isWhite != this.isWhite) {
                // Opponent piece at this square, legal but nothing beyond is legal
                movesList.add(new Move(board, this, this.file + dist, this.rank - dist));
                break;
            } else {
                // Own piece at this square, illegal
                break;
            }
        }

        return movesList;
    }
}
