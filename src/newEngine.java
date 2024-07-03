import board.Move;
import board.pieces.Piece;

import java.util.ArrayList;

public class newEngine {
    public newBoard board;
    public static int maxDepth = 5;

    public newEngine() {
        this.board = new newBoard();
    }

    public void processMove(String moveString) {
        int oldFile = (int) moveString.charAt(0) - 97;
        int oldRank = Character.getNumericValue(moveString.charAt(1)) - 1;
        int newFile = (int) moveString.charAt(2) - 97;
        int newRank = Character.getNumericValue(moveString.charAt(3)) - 1;
        this.board.pieces[newFile][newRank] = this.board.pieces[oldFile][oldRank];
        this.board.pieces[oldFile][oldRank] = 0;
        this.board.colors[newFile][newRank] = this.board.colors[oldFile][oldRank];
        this.board.colors[oldFile][oldRank] = 0;
    }

    public newMove getBestMove(int depth, double alpha, double beta) {
        if (depth <= 0) {
            // This is just a dummy move; all we care about is the evaluation
            newMove onlyMove = new newMove(0, 0, 0, 0);
            onlyMove.evaluation = evaluateBoard(this.board);
            return onlyMove;
        }

        ArrayList<newMove> moves;
        newMove bestMove = null;

        // TODO: Extend support to engine playing white
        if (this.board.whiteTurn) {
            moves = this.board.getMoves('W');
            for (newMove move: moves) {
                char movingPiece = this.board.getPiece(move.oldFile, move.oldRank);
                char movingColor = this.board.getColor(move.oldFile, move.oldRank);
                char capturedPiece = this.board.getPiece(move.newFile, move.newRank);
                char capturedColor = this.board.getColor(move.newFile, move.newRank);
                this.board.doMove(move);
                newMove currMove = getBestMove(depth - 1, alpha, beta);
                if (bestMove == null || currMove.evaluation > bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation > beta) {
                    break;
                } else if (bestMove.evaluation > alpha) {
                    alpha = bestMove.evaluation;
                }
                this.board.pieces[move.oldFile][move.oldRank] = movingPiece;
                this.board.colors[move.oldFile][move.oldRank] = movingColor;
                this.board.pieces[move.newFile][move.newRank] = capturedPiece;
                this.board.colors[move.newFile][move.newRank] = capturedColor;
            }
        } else {
            moves = this.board.getMoves('B');
            for (newMove move : moves) {
                char movingPiece = this.board.getPiece(move.oldFile, move.oldRank);
                char movingColor = this.board.getColor(move.oldFile, move.oldRank);
                char capturedPiece = this.board.getPiece(move.newFile, move.newRank);
                char capturedColor = this.board.getColor(move.newFile, move.newRank);
                this.board.doMove(move);
                newMove currMove = getBestMove(depth - 1, alpha, beta);
                if (bestMove == null || currMove.evaluation < bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation < alpha ) {
                    break;
                } else if (bestMove.evaluation < beta) {
                    beta = bestMove.evaluation;
                }
                this.board.pieces[move.oldFile][move.oldRank] = movingPiece;
                this.board.colors[move.oldFile][move.oldRank] = movingColor;
                this.board.pieces[move.newFile][move.newRank] = capturedPiece;
                this.board.colors[move.newFile][move.newRank] = capturedColor;
            }
        }

        return bestMove;
    }


    public double evaluateBoard(newBoard board) {
        return 0;
    }
}
