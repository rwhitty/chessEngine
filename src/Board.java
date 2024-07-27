import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    public char[][] pieces;
    public GameState gameState;
    public int evaluation;

    public Board() {
        pieces = new char[8][8];

        this.pieces[0][0] = 'R';
        this.pieces[1][0] = 'N';
        this.pieces[2][0] = 'B';
        this.pieces[3][0] = 'Q';
        this.pieces[4][0] = 'K';
        this.pieces[5][0] = 'B';
        this.pieces[6][0] = 'N';
        this.pieces[7][0] = 'R';

        // Black back rank
        this.pieces[0][7] = 'r';
        this.pieces[1][7] = 'n';
        this.pieces[2][7] = 'b';
        this.pieces[3][7] = 'q';
        this.pieces[4][7] = 'k';
        this.pieces[5][7] = 'b';
        this.pieces[6][7] = 'n';
        this.pieces[7][7] = 'r';

        for (int file = 0; file < 8; file++) {
            this.pieces[file][1] = 'P';
            this.pieces[file][6] = 'p';
        }

        this.gameState = new GameState();
        this.gameState.whiteTurn = true;
    }

    @Override
    public Board clone() {
        Board newBoard = new Board();
        for (int file = 0; file < 8; file++) {
            System.arraycopy(this.pieces[file], 0, newBoard.pieces[file], 0, 8);
        }
        newBoard.gameState = this.gameState.clone();
        return newBoard;
    }

    /**
     * Returns an upper-case character representing the color of the piece at (file, rank)
     * 'W' if white, 'B' if black, 0 if neither
     **/
    public char getColor(int file, int rank) {
        char piece = this.pieces[file][rank];
        if (piece == 0) {
            return 0;
        } else if (!Character.isLowerCase(piece)) {
            return 'W';
        }
        return 'B';
    }

    /**
     * Returns an upper-case character representing the type of piece at (file, rank)
     * 'P' for pawn, 'N' for knight, 'B' for bishop, 'R' for rook, 'Q' for queen, 'K' for king
     */
    public char getPiece(int file, int rank) {
        return Character.toUpperCase(this.pieces[file][rank]);
    }

    public void doMove(Move move) {
        // TODO: Can't castle through check
        if (move.specialType == 'k') {
            doKingSide();
        } else if (move.specialType == 'q') {
            doQueenSide();
        } else if (move.specialType == 'p') {
            this.pieces[move.oldFile][move.oldRank] = 0;
            this.pieces[move.newFile][move.newRank] = 'Q'; // TODO: Promotions for both colors
        } else {
            this.evaluation -= evaluatePiece(move.newFile, move.newRank);
            this.pieces[move.newFile][move.newRank] = this.pieces[move.oldFile][move.oldRank];
            this.pieces[move.oldFile][move.oldRank] = 0;

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
        this.gameState.takeTurn();
    }

    public double evaluatePiece(int file, int rank) {
        double evaluation;
        char piece = getPiece(file, rank);

        if (piece == 0) {
            evaluation = 0;
        } else if (piece == 'P') {
            evaluation = 100;
        } else if (piece == 'R') {
            evaluation = 563;
        } else if (piece == 'N') {
            evaluation = 305;
        } else if (piece == 'B') {
            evaluation = 333;
        } else if (piece == 'Q') {
            evaluation = 950;
        } else {
            evaluation = 100000;
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

                char piece = getPiece(file, rank);
                if (getColor(file, rank) == color) {
                    if (piece == 'P') {
                        moves.addAll(getPawnMoves(file, rank));
                    } else if (piece == 'R') {
                        moves.addAll(getRookMoves(file, rank));
                    } else if (piece == 'N') {
                        moves.addAll(getKnightMoves(file, rank));
                    } else if (piece == 'B') {
                        moves.addAll(getBishopMoves(file, rank));
                    } else if (piece == 'Q') {
                        moves.addAll(getQueenMoves(file, rank));
                    } else if (piece == 'K') {
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
        if (color == 'W' && gameState.whiteKingSide &&
                getPiece(5, 0) == 0 && getPiece(6, 0) == 0) {
            moves.add(new Move(file, rank, file, rank, 'k'));
        } else if (color == 'B' && gameState.blackKingSide &&
                getPiece(5, 7) == 0 && getPiece(6, 7) == 0) {
            moves.add(new Move(file, rank, file, rank, 'k'));
        }

        // Queen-side castle
        if (color == 'W' && gameState.whiteQueenSide &&
                getPiece(3, 0) == 0 && getPiece(2, 0) == 0 && getPiece(1, 0) == 0) {
            moves.add(new Move(file, rank, file, rank, 'q'));
        } else if (color == 'B' && gameState.blackQueenSide &&
                getPiece(3, 7) == 0 && getPiece(2, 7) == 0 && getPiece(1, 7) == 0) {
            moves.add(new Move(file, rank, file, rank, 'q'));
        }

        return moves;
    }

    private void doKingSide() {
        if (this.gameState.whiteTurn) {
            this.pieces[4][0] = 0;
            this.pieces[6][0] = 'K';
            this.pieces[7][0] = 0;
            this.pieces[5][0] = 'R';
        } else {
            this.pieces[4][7] = 0;
            this.pieces[6][7] = 'k';
            this.pieces[7][7] = 0;
            this.pieces[5][7] = 'r';
        }
    }

    private void doQueenSide() {
        if (this.gameState.whiteTurn) {
            this.pieces[4][0] = 0;
            this.pieces[2][0] = 'K';
            this.pieces[0][0] = 0;
            this.pieces[3][0] = 'R';
        } else {
            this.pieces[4][7] = 0;
            this.pieces[2][7] = 'k';
            this.pieces[0][7] = 0;
            this.pieces[3][7] = 'r';
        }
    }

    public boolean kingPresent(char color) {
        if (color == 'W') {
            for (int file = 0; file < 8; file++) {
                for (int rank = 0; rank < 8; rank++) {
                    if (getPiece(file, rank) == 'K' && getColor(file, rank) == color) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (int file = 0; file < 8; file++) {
                for (int rank = 7; rank >= 0; rank--) {
                    if (getPiece(file, rank) == 'K' && getColor(file, rank) == color) {
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
        return Arrays.deepEquals(this.pieces, otherBoard.pieces) &&
                this.gameState.equals(otherBoard.gameState);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.pieces);
    }

}

