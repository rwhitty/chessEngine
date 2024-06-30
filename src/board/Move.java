package board;

import board.pieces.*;

public class Move {
    public String name;
    public Board board;
    public double evaluation;
    public Move(Board board, Piece piece, int newFile, int newRank) {
        if (piece == null) {
            return;
        }
        this.board = board.copyBoard();
        Piece movingPiece = this.board.board[piece.file][piece.rank];
        this.board.board[piece.file][piece.rank] = null;
        this.board.board[newFile][newRank] = movingPiece;
        movingPiece.file = newFile;
        movingPiece.rank = newRank;
        this.board.whiteTurn = !board.whiteTurn;
        this.name = Board.files[piece.file] + Integer.toString(piece.rank + 1) + " -> "
                + Board.files[newFile] + (newRank + 1);
    }
}
