package board;
import board.pieces.*;
public class Board {
    public static final char[] files = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    public Piece[][] board;
    public boolean whiteTurn;

    public Board() {
        this.board = new Piece[8][8];
    }

    public void initBoard() {
        /* In our system, (0, 0) is the bottom-left corner of the board, where a white rook lies
        The first number is the file, the second is the rank, so (1, 0) is the white knight. */

        // White back rank
        this.board[0][0] = new Rook(0, 0, true);
        this.board[1][0] = new Knight(1, 0, true);
        this.board[2][0] = new Bishop(2, 0, true);
        this.board[3][0] = new Queen(3, 0, true);
        this.board[4][0] = new King(4, 0, true);
        this.board[5][0] = new Bishop(5, 0, true);
        this.board[6][0] = new Knight(6, 0, true);
        this.board[7][0] = new Rook(7, 0, true);

        // Black back rank
        this.board[0][7] = new Rook(0, 7, false);
        this.board[1][7] = new Knight(1, 7, false);
        this.board[2][7] = new Bishop(2, 7, false);
        this.board[3][7] = new Queen(3, 7, false);
        this.board[4][7] = new King(4, 7, false);
        this.board[5][7] = new Bishop(5, 7, false);
        this.board[6][7] = new Knight(6, 7, false);
        this.board[7][7] = new Rook(7, 7, false);

        // Pawns for both sides
        for (int file = 0; file < 8; file++) {
            this.board[file][1] = new Pawn(file, 1, true);
            this.board[file][6] = new Pawn(file, 6, false);
        }

        this.whiteTurn = true;
    }

    public Piece getPiece(int file, int rank) {
        if (0 <= file && file <= 7 && 0 <= rank && rank <= 7) {
            return this.board[file][rank];
        }
        return null;
    }

    public Board copyBoard() {
        Board newBoard = new Board();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece currPiece = this.getPiece(file, rank);
                if (currPiece == null) {
                    newBoard.board[file][rank] = null;
                } else if (currPiece.name == "pawn") {
                    newBoard.board[file][rank] = new Pawn(file, rank, currPiece.isWhite);
                } else if (currPiece.name == "knight") {
                    newBoard.board[file][rank] = new Knight(file, rank, currPiece.isWhite);
                } else if (currPiece.name == "bishop") {
                    newBoard.board[file][rank] = new Bishop(file, rank, currPiece.isWhite);
                } else if (currPiece.name == "rook") {
                    newBoard.board[file][rank] = new Rook(file, rank, currPiece.isWhite);
                } else if (currPiece.name == "queen") {
                    newBoard.board[file][rank] = new Queen(file, rank, currPiece.isWhite);
                } else {
                    newBoard.board[file][rank] = new King(file, rank, currPiece.isWhite);
                }
            }
        }
        newBoard.whiteTurn = this.whiteTurn;
        return newBoard;
    }
}
