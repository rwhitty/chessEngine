import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
    public Board board;
    public static int maxDepth = 1;


    public Engine() {
        this.board = new Board();
    }

    public void processMove(String moveString) {

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
        Move bestMove = getBestMove(maxDepth, -1000000, 1000000);

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
            // Lost king
            if (this.board.evaluation < -90000) {
                Move onlyMove = new Move(0, 0, 0, 0, 'n');
                onlyMove.evaluation = -100000;
                return onlyMove;
            }

            moves = this.board.getMoves('W');
            for (Move move: moves) {

                char oldPiece = board.pieces[move.oldFile][move.oldRank];
                char newPiece = board.pieces[move.newFile][move.newRank];
                int oldEval = board.evaluation;
                // GameState oldState = board.gameState.clone();

                this.board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                if (move.specialType == 'k') {
                    board.pieces[4][0] = 'K';
                    board.pieces[6][0] = 0;
                    board.pieces[7][0] = 'R';
                    board.pieces[5][0] = 0;
                } else if (move.specialType == 'q') {
                    board.pieces[4][0] = 'K'; // TODO: Possible bug: is castling marked as impossible after castling?
                    board.pieces[2][0] = 0;
                    board.pieces[0][0] = 'R';
                    board.pieces[3][0] = 0;
                } else {
                    board.pieces[move.oldFile][move.oldRank] = oldPiece;
                    board.pieces[move.newFile][move.newRank] = newPiece;
                    board.evaluation = oldEval;
                    board.gameState.whiteTurn = true;
                    // board.gameState = oldState;
                }

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
            // Lost king
            if (this.board.evaluation > 90000) {
                Move onlyMove = new Move(0, 0, 0, 0, 'n');
                onlyMove.evaluation = 100000;
                return onlyMove;
            }

            moves = this.board.getMoves('B');

            for (Move move : moves) {

                int oldEval = board.evaluation;
                char oldPiece = board.pieces[move.oldFile][move.oldRank];
                char newPiece = board.pieces[move.newFile][move.newRank];
                // GameState oldState = board.gameState.clone();

                this.board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                if (move.specialType == 'k') {
                    board.pieces[4][7] = 'k';
                    board.pieces[6][7] = 0;
                    board.pieces[7][7] = 'r';
                    board.pieces[5][7] = 0;
                } else if (move.specialType == 'q') {
                    board.pieces[4][7] = 'k';
                    board.pieces[2][7] = 0;
                    board.pieces[0][7] = 'r';
                    board.pieces[3][7] = 0;
                } else {
                    board.pieces[move.oldFile][move.oldRank] = oldPiece;
                    board.pieces[move.newFile][move.newRank] = newPiece;
                    board.evaluation = oldEval;
                    board.gameState.whiteTurn = false;
                    // board.gameState = oldState;
                }

                if (depth == maxDepth) {
                    System.out.println((char) (move.oldFile + 97) + Integer.toString(move.oldRank + 1) +
                            " -> " + (char) (move.newFile + 97) + (move.newRank + 1));
                    System.out.println(currMove.evaluation);
                }

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

        if (depth == maxDepth)
            System.out.println();

        return bestMove;
    }


    public int evaluateBoard(Board board) {

        int evaluation = 0;
        evaluation += board.evaluation;

        return evaluation;
    }

}
