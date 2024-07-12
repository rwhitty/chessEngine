import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board {
    public char[][] pieces;
    public char[][] colors;
    public GameState gameState;

    public Board() {
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

        this.gameState = new GameState();
    }

    public char getColor(int file, int rank) {
        return this.colors[file][rank];
    }

    public char getPiece(int file, int rank) {
        return this.pieces[file][rank];
    }

    public void doMove(Move move) {
        // TODO: Can't castle through check
        if (move.specialType == 'k') {
            doKingSide();
            return;
        } else if (move.specialType == 'q') {
            doQueenSide();
            return;
        } else if (move.specialType == 'p') {
            this.pieces[move.oldFile][move.oldRank] = 'Q'; // TODO: Promotions other than queen
        }
        this.pieces[move.newFile][move.newRank] = this.pieces[move.oldFile][move.oldRank];
        this.colors[move.newFile][move.newRank] = this.colors[move.oldFile][move.oldRank];
        this.pieces[move.oldFile][move.oldRank] = 0;
        this.colors[move.oldFile][move.oldRank] = 0;
        this.gameState.takeTurn();

        if (move.oldFile == 7 && move.oldRank == 0 || move.oldFile == 4 && move.oldRank == 0) {
            this.gameState.whiteKingSide = false;
        } else if (move.oldFile == 0 && move.oldRank == 0 || move.oldFile == 4 && move.oldRank == 0) {
            this.gameState.whiteQueenSide = false;
        } else if (move.oldFile == 7 && move.oldRank == 7 || move.oldFile == 4 && move.oldRank == 7) {
            this.gameState.blackKingSide = false;
        } else if (move.oldFile == 0 && move.oldRank == 7 || move.oldFile == 4 && move.oldRank == 7) {
            this.gameState.blackQueenSide = false;
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

    public ArrayList<Move> getMoves(char color) {
        ArrayList<Move> moves = new ArrayList<>();
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

    private ArrayList<Move> getQueenMoves(int file, int rank) {
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
        if (this.gameState.whiteTurn && this.gameState.whiteKingSide &&
                getPiece(5, 0) == 0 && getPiece(6, 0) == 0) {
            moves.add(new Move(file, rank, file, rank, 'k'));
        } else if (!this.gameState.whiteTurn && this.gameState.blackKingSide &&
                getPiece(5, 7) == 0 && getPiece(6, 7) == 0) {
            moves.add(new Move(file, rank, file, rank, 'k'));
        }

        // Queen-side castle
        if (this.gameState.whiteTurn && this.gameState.whiteQueenSide &&
                getPiece(3, 0) == 0 && getPiece(2, 0) == 0 && getPiece(1, 0) == 0) {
            moves.add(new Move(file, rank, file, rank, 'q'));
        } else if (!this.gameState.whiteTurn && this.gameState.blackQueenSide &&
                getPiece(3, 7) == 0 && getPiece(2, 7) == 0 && getPiece(1, 7) == 0) {
            moves.add(new Move(file, rank, file, rank, 'q'));
        }

        return moves;
    }

    private void doKingSide() {
        if (this.gameState.whiteTurn) {
            this.pieces[4][0] = this.colors[4][0] = 0;
            this.pieces[6][0] = 'K';
            this.colors[6][0] = 'W';
            this.pieces[7][0] = this.colors[7][0] = 0;
            this.pieces[5][0] = 'R';
            this.colors[5][0] = 'W';
        } else {
            this.pieces[4][7] = this.colors[4][7] = 0;
            this.pieces[6][7] = 'K';
            this.colors[6][7] = 'B';
            this.pieces[7][7] = this.colors[7][7] = 0;
            this.pieces[5][7] = 'R';
            this.colors[5][7] = 'B';
        }
    }

    private void doQueenSide() {
        if (this.gameState.whiteTurn) {
            this.pieces[4][0] = this.colors[4][0] = 0;
            this.pieces[2][0] = 'K';
            this.colors[2][0] = 'W';
            this.pieces[0][0] = this.colors[0][0] = 0;
            this.pieces[3][0] = 'R';
            this.colors[3][0] = 'W';
        } else {
            this.pieces[4][7] = this.colors[4][7] = 0;
            this.pieces[2][7] = 'K';
            this.colors[2][7] = 'B';
            this.pieces[0][7] = this.colors[0][7] = 0;
            this.pieces[3][7] = 'R';
            this.colors[3][7] = 'B';
        }
    }

    public boolean kingPresent(char color) {
        if (color == 'W') {
            for (int file = 0; file < 8; file++) {
                for (int rank = 0; rank < 8; rank++) {
                    if (this.pieces[file][rank] == 'K' && this.colors[file][rank] == color) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (int file = 0; file < 8; file++) {
                for (int rank = 7; rank >= 0; rank--) {
                    if (this.pieces[file][rank] == 'K' && this.colors[file][rank] == color) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) {
            return false;
        }
        Board otherBoard = (Board) obj;
        return Arrays.equals(this.pieces, otherBoard.pieces) && Arrays.equals(this.colors, otherBoard.colors) &&
                this.gameState.equals(otherBoard.gameState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(this.pieces), Arrays.deepHashCode(this.colors));
    }

    public char[][] piecesCopy() {
        char[][] newPieces = new char[8][8];
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                newPieces[file][rank] = pieces[file][rank];
            }
        }
        return newPieces;
    }

    public char[][] colorsCopy() {
        char[][] newColors = new char[8][8];
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                newColors[file][rank] = colors[file][rank];
            }
        }
        return newColors;
    }
}
