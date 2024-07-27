import java.util.ArrayList;

public class newBoard {
    public char[] boardState;
    public int evaluation;

    public newBoard() {
        boardState = new char[72];

        boardState[0] = 'R';
        boardState[1] = 'N';
        boardState[2] = 'B';
        boardState[3] = 'Q';
        boardState[4] = 'K';
        boardState[5] = 'B';
        boardState[6] = 'N';
        boardState[7] = 'R';

        boardState[56] = 'r';
        boardState[57] = 'n';
        boardState[58] = 'b';
        boardState[59] = 'q';
        boardState[60] = 'k';
        boardState[61] = 'b';
        boardState[62] = 'n';
        boardState[63] = 'r';

        for (int i = 8; i < 16; i++) {
            boardState[i] = 'P';
            boardState[i + 40] = 'p';
        }

        boardState[64] = 1; // white turn (0 is black)
        boardState[65] = 1; // white king-side castle legal
        boardState[66] = 1; // white queen-side castle legal
        boardState[67] = 1; // black king-side castle legal
        boardState[68] = 1; // black queen-side castle legal
        boardState[69] = 0; // Row for en-passant
        boardState[70] = 1; // white king present
        boardState[71] = 1; // black king present

        evaluation = 0;
    }

    public char getPiece(int file, int rank) {
        return boardState[8 * rank + file];
    }

    public char getColor(int file, int rank) {
        char piece = boardState[8 * rank + file];
        if (piece == 0) {
            return 0;
        } else if (piece == 'p' || piece == 'n' || piece == 'b' ||
                piece == 'r' || piece == 'q' || piece == 'k') {
            return 'B';
        } else {
            return 'W';
        }
    }

    public void placePiece(int file, int rank, char piece) {
        boardState[8 * rank + file] = piece;
    }

    public boolean whiteTurn() {
        return boardState[64] == 1;
    }

    public boolean kingPresent(char color) {
        if (color == 'W') {
            return boardState[70] == 1;
        } else {
            return boardState[71] == 1;
        }
    }

    public void doMove(Move move) {

        if (move.specialType == 'k') {
            // King-side castle
            if (getColor(move.oldFile, move.oldRank) == 'W') {
                placePiece(4, 0, (char) 0);
                placePiece(6, 0, 'K');
                placePiece(7, 0, (char) 0);
                placePiece(5, 0, 'R');
                boardState[65] = 0;
            } else {
                placePiece(4, 7, (char) 0);
                placePiece(6, 7, 'k');
                placePiece(7, 7, (char) 0);
                placePiece(5, 7, 'r');
                boardState[67] = 0;
            }
            updateGameState(move);
        } else if (move.specialType == 'q') {
            if (getColor(move.oldFile, move.oldRank) == 'W') {
                placePiece(4, 0, (char) 0);
                placePiece(2, 0, 'K');
                placePiece(0, 0, (char) 0);
                placePiece(3, 0, 'R');
                boardState[66] = 0;
            } else {
                placePiece(4, 7, (char) 0);
                placePiece(2, 7, 'k');
                placePiece(0, 7, (char) 0);
                placePiece(3, 7, 'r');
                boardState[68] = 0;
            }
            updateGameState(move);
        } else if (move.specialType == 'p') {
            if (getColor(move.oldFile, move.oldRank) == 'W') {
                placePiece(move.oldFile, move.oldRank, 'Q');
                evaluation += 835;
            } else {
                placePiece(move.oldFile, move.oldRank, 'q');
                evaluation -= 835;
            }
            move.specialType = 'n';
            doMove(move);
        } else {
            char movingPiece = getPiece(move.oldFile, move.oldRank);
            placePiece(move.oldFile, move.oldRank, (char) 0);
            placePiece(move.newFile, move.newRank, movingPiece);
            updateGameState(move);
        }
    }

    public ArrayList<Move> getMoves(char color) {
        ArrayList<Move> moves = new ArrayList<>();

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {

                char piece = getPiece(file, rank);
                if (getColor(file, rank) == color) {
                    if (piece == 'P' || piece == 'p') {
                        moves.addAll(getPawnMoves(file, rank));
                    } else if (piece == 'R' || piece == 'r') {
                        moves.addAll(getRookMoves(file, rank));
                    } else if (piece == 'N' || piece == 'n') {
                        moves.addAll(getKnightMoves(file, rank));
                    } else if (piece == 'B' || piece == 'b') {
                        moves.addAll(getBishopMoves(file, rank));
                    } else if (piece == 'Q' || piece == 'q') {
                        moves.addAll(getQueenMoves(file, rank));
                    } else if (piece == 'K' || piece == 'k') {
                        moves.addAll(getKingMoves(file, rank));
                    }
                }
            }
        }
        return moves;
    }

    private ArrayList<Move> getPawnMoves(int file, int rank) {

        ArrayList<Move> moves = new ArrayList<>();
        char specialType;

        if (getColor(file, rank) == 'W') {

            if (rank < 6) {
                specialType = 'n';
            } else {
                specialType = 'p';
            }

            if (rank < 7 && getPiece(file, rank + 1) == 0) {
                moves.add(new Move(file, rank, file, rank + 1, specialType));
            }
            if (rank == 1 && getPiece(file, rank + 1) == 0 && getPiece(file, rank + 2) == 0) {
                moves.add(new Move(file, rank, file, rank + 2, specialType));
            }
            if (rank < 7 && file > 0 && getColor(file - 1, rank + 1) == 'B') {
                moves.add(new Move(file, rank, file - 1, rank + 1, specialType));
            }
            if (rank < 7 && file < 7 && getColor(file + 1, rank + 1) == 'B') {
                moves.add(new Move(file, rank, file + 1, rank + 1, specialType));
            }
            // TODO: Implement en passant
        } else {

            if (rank > 1) {
                specialType = 'n';
            } else {
                specialType = 'p';
            }

            if (rank > 0 && getPiece(file, rank - 1) == 0) {
                moves.add(new Move(file, rank, file, rank - 1, specialType));
            }
            if (rank == 6 && getPiece(file, rank - 1) == 0 && getPiece(file, rank - 2) == 0) {
                moves.add(new Move(file, rank, file, rank - 2, specialType));
            }
            if (rank > 0 && file > 0 && getColor(file - 1, rank - 1) == 'W') {
                moves.add(new Move(file, rank, file - 1, rank - 1, specialType));
            }
            if (rank > 0 && file < 7 && getColor(file + 1, rank - 1) == 'W') {
                moves.add(new Move(file, rank, file + 1, rank - 1, specialType));
            }
        }

        return moves;
    }

    private ArrayList<Move> getKnightMoves(int file, int rank) {

        ArrayList<Move> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up-up-right
        if (file + 1 < 8 && rank + 2 < 8 && getColor(file + 1, rank + 2) != color) {
            moves.add(new Move(file, rank, file + 1, rank + 2, 'n'));
        }

        // Going up-right-right
        if (file + 2 < 8 && rank + 1 < 8 && getColor(file + 2, rank + 1) != color) {
            moves.add(new Move(file, rank, file + 2, rank + 1, 'n'));
        }

        // Going down-right-right
        if (file + 2 < 8 && rank - 1 >= 0 && getColor(file + 2, rank - 1) != color) {
            moves.add(new Move(file, rank, file + 2, rank - 1, 'n'));
        }

        // Going down-down-right
        if (file + 1 < 8 && rank - 2 >= 0 && getColor(file + 1, rank - 2) != color) {
            moves.add(new Move(file, rank, file + 1, rank - 2, 'n'));
        }

        // Going down-down-left
        if (file - 1 >= 0 && rank - 2 >= 0 && getColor(file - 1, rank - 2) != color) {
            moves.add(new Move(file, rank, file - 1, rank - 2, 'n'));
        }

        // Going down-left-left
        if (file - 2 >= 0 && rank - 1 >= 0 && getColor(file - 2, rank - 1) != color) {
            moves.add(new Move(file, rank, file - 2, rank - 1, 'n'));
        }

        // Going up-left-left
        if (file - 2 >= 0 && rank + 1 < 8 && getColor(file - 2, rank + 1) != color) {
            moves.add(new Move(file, rank, file - 2, rank + 1, 'n'));
        }

        // Going up-up-left
        if (file - 1 >= 0 && rank + 2 < 8 && getColor(file - 1, rank + 2) != color) {
            moves.add(new Move(file, rank, file - 1, rank + 2, 'n'));
        }

        return moves;
    }

    private ArrayList<Move> getBishopMoves(int file, int rank) {

        ArrayList<Move> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up-right
        for (int dist = 1; file + dist < 8 && rank + dist < 8; dist++) {
            if (getColor(file + dist, rank + dist) != color) {
                moves.add(new Move(file, rank, file + dist, rank + dist, 'n'));
                if (getColor(file + dist, rank + dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        // Going down-right
        for (int dist = 1; file + dist < 8 && rank - dist >= 0; dist++) {
            if (getColor(file + dist, rank - dist) != color) {
                moves.add(new Move(file, rank, file + dist, rank - dist, 'n'));
                if (getColor(file + dist, rank - dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        // Going down-left
        for (int dist = 1; file - dist >= 0 && rank - dist >= 0; dist++) {
            if (getColor(file - dist, rank - dist) != color) {
                moves.add(new Move(file, rank, file - dist, rank - dist, 'n'));
                if (getColor(file - dist, rank - dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        // Going up-left
        for (int dist = 1; file - dist >= 0 && rank + dist < 8; dist++) {
            if (getColor(file - dist, rank + dist) != color) {
                moves.add(new Move(file, rank, file - dist, rank + dist, 'n'));
                if (getColor(file - dist, rank + dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }

    private ArrayList<Move> getRookMoves(int file, int rank) {

        ArrayList<Move> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up
        for (int dist = 1; rank + dist < 8; dist++) {
            if (getColor(file, rank + dist) != color) {
                moves.add(new Move(file, rank, file, rank + dist, 'n'));
                if (getColor(file, rank + dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        // Going right
        for (int dist = 1; file + dist < 8; dist++) {
            if (getColor(file + dist, rank) != color) {
                moves.add(new Move(file, rank, file + dist, rank, 'n'));
                if (getColor(file + dist, rank) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        // Going down
        for (int dist = 1; rank - dist >= 0; dist++) {
            if (getColor(file, rank - dist) != color) {
                moves.add(new Move(file, rank, file, rank - dist, 'n'));
                if (getColor(file, rank - dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        // Going left
        for (int dist = 1; file - dist >= 0; dist++) {
            if (getColor(file - dist, rank) != color) {
                moves.add(new Move(file, rank, file - dist, rank, 'n'));
                if (getColor(file - dist, rank) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }

    private ArrayList<Move> getQueenMoves(int file, int rank) {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getBishopMoves(file, rank));
        moves.addAll(getRookMoves(file, rank));
        return moves;
    }

    private ArrayList<Move> getKingMoves(int file, int rank) {
        ArrayList<Move> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up
        if (rank < 7 && getColor(file, rank + 1) != color) {
            moves.add(new Move(file, rank, file, rank + 1, 'n'));
        }

        // Going up-right
        if (file < 7 && rank < 7 && getColor(file + 1, rank + 1) != color) {
            moves.add(new Move(file, rank, file + 1, rank + 1, 'n'));
        }

        // Going right
        if (file < 7 && getColor(file + 1, rank) != color) {
            moves.add(new Move(file, rank, file + 1 ,rank, 'n'));
        }

        // Going down-right
        if (file < 7 && rank > 0 && getColor(file + 1, rank - 1) != color) {
            moves.add(new Move(file, rank, file + 1, rank - 1, 'n'));
        }

        // Going down
        if (rank > 0 && getColor(file, rank - 1) != color) {
            moves.add(new Move(file, rank, file, rank - 1, 'n'));
        }

        // Going down-left
        if (file > 0 && rank > 0 && getColor(file - 1, rank - 1) != color) {
            moves.add(new Move(file, rank, file - 1, rank - 1, 'n'));
        }

        // Going left
        if (file > 0 && getColor(file - 1, rank) != color) {
            moves.add(new Move(file, rank, file - 1, rank, 'n'));
        }

        // Going up-left
        if (file > 0 && rank < 7 && getColor(file - 1, rank + 1) != color) {
            moves.add(new Move(file, rank, file - 1, rank + 1, 'n'));
        }

        // King-side castle
        if (kingSideLegal(color)) {
            moves.add(new Move(file, rank, file + 2, rank, 'k'));
        }

        // Queen-side castle
        if (queenSideLegal(color)) {
            moves.add(new Move(file, rank, file - 2, rank, 'q'));
        }

        return moves;
    }

    private boolean kingSideLegal(char color) {
        // TODO: Need to check for castling through check
        if (color == 'W') {
            return boardState[65] == 1 &&
                    boardState[5] == 0 &&
                    boardState[6] == 0;
        } else {
            return boardState[67] == 1 &&
                    boardState[61] == 0 &&
                    boardState[62] == 0;
        }
    }

    private boolean queenSideLegal(char color) {
        // TODO: Need to check for castling through check
        if (color == 'W') {
            return boardState[66] == 1 &&
                    boardState[3] == 0 &&
                    boardState[2] == 0 &&
                    boardState[1] == 0;
        } else {
            return boardState[68] == 1 &&
                    boardState[59] == 0 &&
                    boardState[58] == 0 &&
                    boardState[57] == 0;
        }
    }

    private void updateGameState(Move move) {

        // Update king presence
        char capturedPiece = getPiece(move.newFile, move.newRank);
        if (capturedPiece == 'K') {
            boardState[70] = 0;
        } else if (capturedPiece == 'k') {
            boardState[71] = 0;
        }

        // Update castle legality
        if (move.oldFile == 4 && move.oldRank == 0) {
            boardState[65] = 0;
            boardState[66] = 0;
        } else if (move.oldFile == 7 && move.oldRank == 0) {
            boardState[65] = 0;
        } else if (move.oldFile == 0 && move.oldRank == 0) {
            boardState[66] = 0;
        } else if (move.oldFile == 4 && move.oldRank == 7) {
            boardState[67] = 0;
            boardState[68] = 0;
        } else if (move.oldFile == 7 && move.oldRank == 7) {
            boardState[67] = 0;
        } else if (move.oldFile == 0 && move.oldRank == 7) {
            boardState[68] = 0;
        }

        evaluation -= evaluate(move.newFile, move.newRank);
        boardState[64] = (char) (1 - boardState[64]);
    }

    private int evaluate(int file, int rank) {

        int evaluation = 0;
        char piece = getPiece(file, rank);
        char color = getColor(file, rank);

        if (piece == 'P' || piece == 'p') {
            evaluation = 100;
        } else if (piece == 'N' || piece == 'n') {
            evaluation = 305;
        } else if (piece == 'B' || piece == 'b') {
            evaluation = 333;
        } else if (piece == 'R' || piece == 'r') {
            evaluation = 563;
        } else if (piece == 'Q' || piece == 'q') {
            evaluation = 950;
        } else if (piece == 'K' || piece == 'k') {
            evaluation = 100000;
        }

        if (color == 'B') {
            evaluation *= -1;
        }

        return evaluation;
    }
}
