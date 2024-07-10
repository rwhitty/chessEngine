import java.util.ArrayList;
import java.util.Arrays;

public class newBoard {
    public char[][] pieces;
    public char[][] colors;
    public boolean whiteTurn;
    public boolean whiteKingMoved;
    public boolean blackKingMoved;
    public boolean whiteARookMoved;
    public boolean whiteHRookMoved;
    public boolean blackARookMoved;
    public boolean blackHRookMoved;

    public newBoard() {
        this.pieces = new char[8][8];
        this.colors = new char[8][8];

        this.pieces[0][0] = 'R';
        this.pieces[1][0] = 'N';
        this.pieces[2][0] = 'B';
        this.pieces[3][0] = 'Q';
        this.pieces[4][0] = 'K';
        this.pieces[5][0] = 'B';
        this.pieces[6][0] = 'N';
        this.pieces[7][0] = 'R';

        // Black back rank
        this.pieces[0][7] = 'R';
        this.pieces[1][7] = 'N';
        this.pieces[2][7] = 'B';
        this.pieces[3][7] = 'Q';
        this.pieces[4][7] = 'K';
        this.pieces[5][7] = 'B';
        this.pieces[6][7] = 'N';
        this.pieces[7][7] = 'R';

        for (int file = 0; file < 8; file++) {
            this.pieces[file][1] = 'P';
            this.pieces[file][6] = 'P';
        }

        for (int file = 0; file < 8; file++) {
            this.colors[file][0] = 'W';
            this.colors[file][1] = 'W';
            this.colors[file][6] = 'B';
            this.colors[file][7] = 'B';
        }

        this.whiteTurn = true;
    }

    public char getColor(int file, int rank) {
        return this.colors[file][rank];
    }

    public char getPiece(int file, int rank) {
        return this.pieces[file][rank];
    }

    public void doMove(newMove move) {
        this.pieces[move.newFile][move.newRank] = this.pieces[move.oldFile][move.oldRank];
        this.colors[move.newFile][move.newRank] = this.colors[move.oldFile][move.oldRank];
        this.pieces[move.oldFile][move.oldRank] = 0;
        this.colors[move.oldFile][move.oldRank] = 0;
        this.whiteTurn = !this.whiteTurn;

        if (move.oldFile == 0 && move.oldRank == 0) {
            this.whiteARookMoved = true;
        } else if (move.oldFile == 0 && move.oldRank == 7) {
            this.blackARookMoved = true;
        } else if (move.oldFile == 7 && move.oldRank == 0) {
            this.whiteHRookMoved = true;
        } else if (move.oldFile == 7 && move.oldRank == 7) {
            this.blackHRookMoved = true;
        } else if (move.oldFile == 4 && move.oldRank == 0) {
            this.whiteKingMoved = true;
        } else if (move.oldFile == 4 && move.oldRank == 7) {
            this.blackKingMoved = true;
        }
    }

    public double evaluatePiece(int file, int rank) {
        double evaluation;
        if (getPiece(file, rank) == 0) {
            evaluation = 0;
        } else if (getPiece(file, rank) == 'P') {
            evaluation = 1;
        } else if (getPiece(file, rank) == 'R') {
            evaluation = 5.63;
        } else if (getPiece(file, rank) == 'N') {
            evaluation = 3.05;
        } else if (getPiece(file, rank) == 'B') {
            evaluation = 3.33;
        } else if (getPiece(file, rank) == 'Q') {
            evaluation = 9.5;
        } else {
            evaluation = 1000;
        }

        if (getColor(file, rank) == 'B') {
            evaluation = -1 * evaluation;
        }

        return evaluation;
    }

    public ArrayList<newMove> getMoves(char color) {
        ArrayList<newMove> moves = new ArrayList<>();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                if (getColor(file, rank) == color) {
                    if (getPiece(file, rank) == 'P') {
                        moves.addAll(getPawnMoves(file, rank));
                    } else if (getPiece(file, rank) == 'R') {
                        moves.addAll(getRookMoves(file, rank));
                    } else if (getPiece(file, rank) == 'N') {
                        moves.addAll(getKnightMoves(file, rank));
                    } else if (getPiece(file, rank) == 'B') {
                        moves.addAll(getBishopMoves(file, rank));
                    } else if (getPiece(file, rank) == 'Q') {
                        moves.addAll(getQueenMoves(file, rank));
                    } else if (getPiece(file, rank) == 'K') {
                        moves.addAll(getKingMoves(file, rank));
                    }
                }
            }
        }
        return moves;
    }

    private ArrayList<newMove> getPawnMoves(int file, int rank) {
        ArrayList<newMove> moves = new ArrayList<>();

        if (getColor(file, rank) == 'W') {
            if (rank < 7 && getPiece(file, rank + 1) == 0) {
                // TODO: Implement promotion
                moves.add(new newMove(file, rank, file, rank + 1, 'n'));
            }
            if (rank == 1 && getPiece(file, rank + 1) == 0 && getPiece(file, rank + 2) == 0) {
                moves.add(new newMove(file, rank, file, rank + 2, 'n'));
            }
            if (rank < 7 && file > 0 && getColor(file - 1, rank + 1) == 'B') {
                moves.add(new newMove(file, rank, file - 1, rank + 1, 'n'));
            }
            if (rank < 7 && file < 7 && getColor(file + 1, rank + 1) == 'B') {
                moves.add(new newMove(file, rank, file + 1, rank + 1, 'n'));
            }
            // TODO: Implement en passant
        } else {
            if (rank > 0 && getPiece(file, rank - 1) == 0) {
                moves.add(new newMove(file, rank, file, rank - 1, 'n'));
            }
            if (rank == 6 && getPiece(file, rank - 1) == 0 && getPiece(file, rank - 2) == 0) {
                moves.add(new newMove(file, rank, file, rank - 2, 'n'));
            }
            if (rank > 0 && file > 0 && getColor(file - 1, rank - 1) == 'W') {
                moves.add(new newMove(file, rank, file - 1, rank - 1, 'n'));
            }
            if (rank > 0 && file < 7 && getColor(file + 1, rank - 1) == 'W') {
                moves.add(new newMove(file, rank, file + 1, rank - 1, 'n'));
            }
        }

        return moves;
    }

    private ArrayList<newMove> getRookMoves(int file, int rank) {
        ArrayList<newMove> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up
        for (int dist = 1; rank + dist < 8; dist++) {
            if (getColor(file, rank + dist) != color) {
                moves.add(new newMove(file, rank, file, rank + dist, 'n'));
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
                moves.add(new newMove(file, rank, file + dist, rank, 'n'));
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
                moves.add(new newMove(file, rank, file, rank - dist, 'n'));
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
                moves.add(new newMove(file, rank, file - dist, rank, 'n'));
                if (getColor(file - dist, rank) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }

    private ArrayList<newMove> getKnightMoves(int file, int rank) {
        ArrayList<newMove> moves = new ArrayList<>();
        char color = getColor(file, rank);
        // Going up-up-right
        if (file + 1 < 8 && rank + 2 < 8 && getColor(file + 1, rank + 2) != color) {
            moves.add(new newMove(file, rank, file + 1, rank + 2, 'n'));
        }

        // Going up-right-right
        if (file + 2 < 8 && rank + 1 < 8 && getColor(file + 2, rank + 1) != color) {
            moves.add(new newMove(file, rank, file + 2, rank + 1, 'n'));
        }

        // Going down-right-right
        if (file + 2 < 8 && rank - 1 >= 0 && getColor(file + 2, rank - 1) != color) {
            moves.add(new newMove(file, rank, file + 2, rank - 1, 'n'));
        }

        // Going down-down-right
        if (file + 1 < 8 && rank - 2 >= 0 && getColor(file + 1, rank - 2) != color) {
            moves.add(new newMove(file, rank, file + 1, rank - 2, 'n'));
        }

        // Going down-down-left
        if (file - 1 >= 0 && rank - 2 >= 0 && getColor(file - 1, rank - 2) != color) {
            moves.add(new newMove(file, rank, file - 1, rank - 2, 'n'));
        }

        // Going down-left-left
        if (file - 2 >= 0 && rank - 1 >= 0 && getColor(file - 2, rank - 1) != color) {
            moves.add(new newMove(file, rank, file - 2, rank - 1, 'n'));
        }

        // Going up-left-left
        if (file - 2 >= 0 && rank + 1 < 8 && getColor(file - 2, rank + 1) != color) {
            moves.add(new newMove(file, rank, file - 2, rank + 1, 'n'));
        }

        // Going up-up-left
        if (file - 1 >= 0 && rank + 2 < 8 && getColor(file - 1, rank + 2) != color) {
            moves.add(new newMove(file, rank, file - 1, rank + 2, 'n'));
        }

        return moves;
    }

    private ArrayList<newMove> getBishopMoves(int file, int rank) {
        ArrayList<newMove> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up-right
        for (int dist = 1; file + dist < 8 && rank + dist < 8; dist++) {
            if (getColor(file + dist, rank + dist) != color) {
                moves.add(new newMove(file, rank, file + dist, rank + dist, 'n'));
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
                moves.add(new newMove(file, rank, file + dist, rank - dist, 'n'));
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
                moves.add(new newMove(file, rank, file - dist, rank - dist, 'n'));
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
                moves.add(new newMove(file, rank, file - dist, rank + dist, 'n'));
                if (getColor(file - dist, rank + dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }

    private ArrayList<newMove> getQueenMoves(int file, int rank) {
        ArrayList<newMove> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up
        for (int dist = 1; rank + dist < 8; dist++) {
            if (getColor(file, rank + dist) != color) {
                moves.add(new newMove(file, rank, file, rank + dist, 'n'));
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
                moves.add(new newMove(file, rank, file + dist, rank, 'n'));
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
                moves.add(new newMove(file, rank, file, rank - dist, 'n'));
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
                moves.add(new newMove(file, rank, file - dist, rank, 'n'));
                if (getColor(file - dist, rank) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        // Going up-right
        for (int dist = 1; file + dist < 8 && rank + dist < 8; dist++) {
            if (getColor(file + dist, rank + dist) != color) {
                moves.add(new newMove(file, rank, file + dist, rank + dist, 'n'));
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
                moves.add(new newMove(file, rank, file + dist, rank - dist, 'n'));
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
                moves.add(new newMove(file, rank, file - dist, rank - dist, 'n'));
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
                moves.add(new newMove(file, rank, file - dist, rank + dist, 'n'));
                if (getColor(file - dist, rank + dist) != 0) {
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }

    private ArrayList<newMove> getKingMoves(int file, int rank) {
        ArrayList<newMove> moves = new ArrayList<>();
        char color = getColor(file, rank);

        // Going up
        if (rank < 7 && getColor(file, rank + 1) != color) {
            moves.add(new newMove(file, rank, file, rank + 1, 'n'));
        }

        // Going up-right
        if (file < 7 && rank < 7 && getColor(file + 1, rank + 1) != color) {
            moves.add(new newMove(file, rank, file + 1, rank + 1, 'n'));
        }

        // Going right
        if (file < 7 && getColor(file + 1, rank) != color) {
            moves.add(new newMove(file, rank, file + 1 ,rank, 'n'));
        }

        // Going down-right
        if (file < 7 && rank > 0 && getColor(file + 1, rank - 1) != color) {
            moves.add(new newMove(file, rank, file + 1, rank - 1, 'n'));
        }

        // Going down
        if (rank > 0 && getColor(file, rank - 1) != color) {
            moves.add(new newMove(file, rank, file, rank - 1, 'n'));
        }

        // Going down-left
        if (file > 0 && rank > 0 && getColor(file - 1, rank - 1) != color) {
            moves.add(new newMove(file, rank, file - 1, rank - 1, 'n'));
        }

        // Going left
        if (file > 0 && getColor(file - 1, rank) != color) {
            moves.add(new newMove(file, rank, file - 1, rank, 'n'));
        }

        // Going up-left
        if (file > 0 && rank < 7 && getColor(file - 1, rank + 1) != color) {
            moves.add(new newMove(file, rank, file - 1, rank + 1, 'n'));
        }

        // King-side castle
        if (whiteTurn && !whiteKingMoved && !whiteHRookMoved &&
                getPiece(5, 0) == 0 && getPiece(6, 0) == 0) {
            moves.add(new newMove(file, rank, file, rank, 'k'));
        } else if (!whiteTurn && !blackKingMoved && !blackHRookMoved &&
                getPiece(5, 7) == 0 && getPiece(6, 7) == 0) {
            moves.add(new newMove(file, rank, file, rank, 'k'));
        }

        // Queen-side castle
        if (whiteTurn && !whiteKingMoved && !whiteARookMoved &&
                getPiece(3, 0) == 0 && getPiece(2, 0) == 0 && getPiece(1, 0) == 0) {
            moves.add(new newMove(file, rank, file, rank, 'q'));
        } else if (!whiteTurn && !blackKingMoved && !blackHRookMoved &&
                getPiece(3, 7) == 0 && getPiece(2, 7) == 0 && getPiece(1, 7) == 0) {
            moves.add(new newMove(file, rank, file, rank, 'q'));
        }

        return moves;
    }
}
