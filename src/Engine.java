import board.pieces.*;
import board.*;
import java.util.*;
public class Engine {
    public Board board;
    public static int maxDepth = 5;
    public Engine() {
        this.board = new Board();
        this.board.initBoard();
    }

    public void processMove(String moveString) {
        int oldFile = (int) moveString.charAt(0) - 97;
        int oldRank = Character.getNumericValue(moveString.charAt(1)) - 1;
        int newFile = (int) moveString.charAt(2) - 97;
        int newRank = Character.getNumericValue(moveString.charAt(3)) - 1;
        Piece movingPiece = this.board.getPiece(oldFile, oldRank);
        Move move = new Move(this.board, movingPiece, newFile, newRank);
        this.board = move.board;
    }

    public String makeMove() {
        Move bestMove = bestMove(board, maxDepth, -10000, 10000);
        this.board = bestMove.board;
        return bestMove.name;
    }

    // Returns the best move
    public Move bestMove(Board board, int depth, double alpha, double beta) {

        if (depth <= 0) {
            // This is just a dummy move; all we care about is the evaluation
            Move onlyMove = new Move(board, null, 0, 0);
            onlyMove.evaluation = evaluateBoard(board);
            return onlyMove;
        }

        List<Move> movesList = new ArrayList<>();

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece currPiece = board.getPiece(file, rank);
                if (currPiece != null && currPiece.isWhite == board.whiteTurn) {
                    List<Move> currPieceMoves = currPiece.getMoves(board);
                    for (Move currMove: currPieceMoves) {
                        movesList.add(currMove);
                    }
                }
            }
        }

        Move bestMove = null;

        if (board.whiteTurn) {
            for (Move move: movesList) {
                Move currMove = bestMove(move.board, depth - 1, alpha, beta);
                if (bestMove == null || currMove.evaluation > bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation > beta) {
                    break;
                } else if (bestMove.evaluation > alpha){
                    alpha = bestMove.evaluation;
                }
            }
        } else {
            for (Move move: movesList) {
                Move currMove = bestMove(move.board, depth - 1, alpha, beta);
                if (bestMove == null || currMove.evaluation < bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation < alpha) {
                    break;
                } else if (bestMove.evaluation < beta){
                    beta = bestMove.evaluation;
                }
            }
        }

        return bestMove;
    }

    public double evaluateBoard(Board board) {
        double evaluation = 0;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece currPiece = board.getPiece(file, rank);
                if (currPiece == null) {
                    continue;
                } else if (currPiece.isWhite) {
                    evaluation += currPiece.value;
                    evaluation += currPiece.getMoves(board).size() * 0.02;
                    if (3 <= file && file <= 4 && 3 <= rank && rank <= 4) {
                        evaluation += 0.1;
                    }
                } else {
                    evaluation -= currPiece.value;
                    evaluation -= currPiece.getMoves(board).size() * 0.02;
                    if (3 <= file && file <= 4 && 3 <= rank && rank <= 4) {
                        evaluation -= 0.1;
                    }
                }
            }
        }

        if (board.whiteTurn == true) {
            evaluation += 0.3;
        } else {
            evaluation -= 0.3;
        }

        // Other ideas: How many total squares do we control?
        // Weight those in the center more
        // Points for pieces being IN the center as well
        // Lose points for doubled pawns
        // Lose points for exposed king

        return evaluation;
    }
}
