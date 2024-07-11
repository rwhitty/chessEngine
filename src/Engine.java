import java.util.ArrayList;

public class Engine {
    public Board board;
    public static int maxDepth = 5;

    public Engine() {
        this.board = new Board();
    }

    public void processMove(String moveString) {
        // TODO: Implement castling here too
        if (moveString.equals("kc")) {
            this.board.doMove(new Move(0, 0, 0, 0, 'k'));
            return;
        } else if (moveString.equals("qc")) {
            this.board.doMove(new Move(0, 0, 0, 0, 'q'));
            return;
        }
        int oldFile = (int) moveString.charAt(0) - 97;
        int oldRank = Character.getNumericValue(moveString.charAt(1)) - 1;
        int newFile = (int) moveString.charAt(2) - 97;
        int newRank = Character.getNumericValue(moveString.charAt(3)) - 1;
        this.board.doMove(new Move(oldFile, oldRank, newFile, newRank, 'n'));
    }

    public String makeMove() {
        Move bestMove = getBestMove(maxDepth, -10000, 10000);
        this.board.doMove(bestMove);
        if (bestMove.specialType == 'k') {
            return "kingside castle";
        } else if (bestMove.specialType == 'q') {
            return "queenside castle";
        }
        return (char) (bestMove.oldFile + 97) + Integer.toString(bestMove.oldRank + 1) +
                " -> " + (char) (bestMove.newFile + 97) + (bestMove.newRank + 1);
    }

    public Move getBestMove(int depth, double alpha, double beta) {
        if (depth <= 0) {
            // This is just a dummy move; all we care about is the evaluation
            Move onlyMove = new Move(0, 0, 0, 0, 'n');
            onlyMove.evaluation = evaluateBoard(this.board);
            return onlyMove;
        }

        ArrayList<Move> moves;
        Move bestMove = null;

        // TODO: Extend support to engine playing white
        if (this.board.gameState.whiteTurn) {
            moves = this.board.getMoves('W');
            for (Move move: moves) {
                char movingPiece = this.board.getPiece(move.oldFile, move.oldRank);
                char movingColor = this.board.getColor(move.oldFile, move.oldRank);
                char capturedPiece = this.board.getPiece(move.newFile, move.newRank);
                char capturedColor = this.board.getColor(move.newFile, move.newRank);
                // TODO: Save other aspects of state, like pieces having moved
                this.board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                this.board.gameState.whiteTurn = true;
                this.board.pieces[move.oldFile][move.oldRank] = movingPiece;
                this.board.colors[move.oldFile][move.oldRank] = movingColor;
                this.board.pieces[move.newFile][move.newRank] = capturedPiece;
                this.board.colors[move.newFile][move.newRank] = capturedColor;

                if (bestMove == null || currMove.evaluation > bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation > beta) {
                    break;
                } else if (bestMove.evaluation > alpha) {
                    alpha = bestMove.evaluation;
                }
            }
        } else {
            moves = this.board.getMoves('B');
            for (Move move : moves) {
                char movingPiece = this.board.getPiece(move.oldFile, move.oldRank);
                char movingColor = this.board.getColor(move.oldFile, move.oldRank);
                char capturedPiece = this.board.getPiece(move.newFile, move.newRank);
                char capturedColor = this.board.getColor(move.newFile, move.newRank);
                this.board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                this.board.gameState.whiteTurn = false;
                this.board.pieces[move.oldFile][move.oldRank] = movingPiece;
                this.board.colors[move.oldFile][move.oldRank] = movingColor;
                this.board.pieces[move.newFile][move.newRank] = capturedPiece;
                this.board.colors[move.newFile][move.newRank] = capturedColor;

                if (bestMove == null || currMove.evaluation < bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation < alpha ) {
                    break;
                } else if (bestMove.evaluation < beta) {
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
                evaluation += board.evaluatePiece(file, rank);
            }
        }

        evaluation += 0.04 * board.getMoves('W').size();
        evaluation -= 0.04 * board.getMoves('B').size();

        if (board.gameState.whiteTurn) {
            evaluation += 0.2;
        } else {
            evaluation -= 0.2;
        }

        for (int file = 3; file < 5; file++) {
            for (int rank = 3; rank < 5; rank++) {
                if (board.getColor(file, rank) == 'W') {
                    evaluation += 0.1;
                } else if (board.getColor(file, rank) == 'B') {
                    evaluation -= 0.1;
                }
            }
        }

        return evaluation;
    }

}
