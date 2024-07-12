import java.util.ArrayList;
import java.util.HashMap;

public class Engine {
    public Board board;
    public static int maxDepth = 6;

    public HashMap<Board, Double> positions;

    public Engine() {
        this.board = new Board();
        this.positions = new HashMap<>();
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
        if (depth == maxDepth) {
            this.positions = new HashMap<>();
        } else if (depth <= 0) {
            // This is just a dummy move; all we care about is the evaluation
            Move onlyMove = new Move(0, 0, 0, 0, 'n');
            onlyMove.evaluation = evaluateBoard(this.board);
            return onlyMove;
        } else if (positions.containsKey(this.board) && depth < maxDepth) {
            Move onlyMove = new Move(0, 0, 0, 0, 'n');
            onlyMove.evaluation = positions.get(this.board);
            return onlyMove;
        }

        ArrayList<Move> moves;
        Move bestMove = null;

        // TODO: Extend support to engine playing white
        if (this.board.gameState.whiteTurn) {
            // Lost king
            if (!this.board.kingPresent('W')) {
                Move onlyMove = new Move(0, 0, 0, 0, 'n');
                onlyMove.evaluation = -1000;
                return onlyMove;
            }

            moves = this.board.getMoves('W');
            for (Move move: moves) {
                char[][] oldPieces = this.board.piecesCopy();
                char[][] oldColors = this.board.colorsCopy();
                GameState oldGameState = this.board.gameState.clone();

                this.board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                this.board.gameState = oldGameState;
                this.board.pieces = oldPieces;
                this.board.colors = oldColors;

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
            if (!this.board.kingPresent('B')) {
                Move onlyMove = new Move(0, 0, 0, 0, 'n');
                onlyMove.evaluation = 1000;
                return onlyMove;
            }

            moves = this.board.getMoves('B');
            for (Move move : moves) {
                char[][] oldPieces = this.board.piecesCopy();
                char[][] oldColors = this.board.colorsCopy();
                GameState oldGameState = this.board.gameState.clone();

                this.board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                this.board.gameState = oldGameState;
                this.board.pieces = oldPieces;
                this.board.colors = oldColors;

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
        positions.put(this.board, bestMove.evaluation);
        return bestMove;
    }


    public double evaluateBoard(Board board) {

        double evaluation = 0;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                evaluation += board.evaluatePiece(file, rank);
            }
        }

        ArrayList<Move> whiteMoves = board.getMoves('W');
        ArrayList<Move> blackMoves = board.getMoves('B');
        evaluation += 0.04 * whiteMoves.size();
        evaluation -= 0.04 * blackMoves.size();

        for (Move move: whiteMoves) {
            if (move.newFile > 2 && move.newFile < 5 && move.newRank > 2 && move.newRank < 5) {
                evaluation += 0.1;
            }
            if (board.colors[move.newFile][move.oldFile] == 'B') {
                evaluation += 0.1;
            }
        }

        for (Move move: blackMoves) {
            if (move.newFile > 2 && move.newFile < 5 && move.newRank > 2 && move.newRank < 5) {
                evaluation -= 0.1;
            }
            if (board.colors[move.newFile][move.oldFile] == 'W') {
                evaluation -= 0.1;
            }
        }

        for (int file = 2; file < 6; file++) {
            for (int rank = 2; rank < 6; rank++) {
                if (board.colors[file][rank] == 'W') {
                    evaluation += 0.05;
                } else if (board.colors[file][rank] == 'B'){
                    evaluation -= 0.05;
                }
            }
        }

        for (int file = 3; file < 5; file++) {
            for (int rank = 3; rank < 5; rank++) {
                if (board.colors[file][rank] == 'W') {
                    evaluation += 0.05;
                } else if (board.colors[file][rank] == 'B'){
                    evaluation -= 0.05;
                }
            }
        }

        if (board.gameState.whiteTurn) {
            evaluation += 0.2;
        } else {
            evaluation -= 0.2;
        }

        return evaluation;
    }

}
