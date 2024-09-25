import java.util.ArrayList;
import java.util.HashMap;

public class Engine {
    public Board board;
    public static int maxDepth = 7;
    public HashMap<String, int[]> positions;

    public Engine() {
        board = new Board();
    }

    public void processMove(String moveString) {

        if (moveString.equals("kc")) {
            this.board.doMove(new Move(4, 0, 6, 0, 'k'));
            return;
        } else if (moveString.equals("qc")) {
            this.board.doMove(new Move(4, 0, 2, 0, 'q'));
            return;
        }
        int oldFile = (int) moveString.charAt(0) - 97;
        int oldRank = Character.getNumericValue(moveString.charAt(1)) - 1;
        int newFile = (int) moveString.charAt(2) - 97;
        int newRank = Character.getNumericValue(moveString.charAt(3)) - 1;
        this.board.doMove(new Move(oldFile, oldRank, newFile, newRank, 'n'));
    }

    public String makeMove() {
        positions = new HashMap<>();
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
        String boardCode = board.boardCode();
        if (depth <= 0) {
            // This is just a dummy move; all we care about is the evaluation
            Move onlyMove = new Move(0, 0, 0, 0, 'n');
            onlyMove.evaluation = evaluateBoard();
            return onlyMove;
        } else if (depth < maxDepth && positions.containsKey(boardCode) && positions.get(boardCode)[1] >= depth) {
            Move onlyMove = new Move(0, 0, 0, 0, 'n');
            onlyMove.evaluation = positions.get(boardCode)[0];
            return onlyMove;
        }

        ArrayList<Move> moves;
        Move bestMove = null;

        // TODO: Extend support to engine playing white
        if (board.whiteTurn()) {
            // Lost king
            if (board.kingMissing('W')) {
                Move onlyMove = new Move(0, 0, 0, 0, 'n');
                onlyMove.evaluation = -1000000;
                return onlyMove;
            }

            moves = this.board.getMoves('W');
            for (Move move: moves) {
                Board oldBoard = board.clone();

                board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                board = oldBoard;

                if (bestMove == null || currMove.evaluation > bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation > beta - 1) {
                    break;
                } else if (bestMove.evaluation > alpha) {
                    alpha = bestMove.evaluation;
                }
            }
        } else {
            // Lost king
            if (board.kingMissing('B')) {
                Move onlyMove = new Move(0, 0, 0, 0, 'n');
                onlyMove.evaluation = 1000000;
                return onlyMove;
            }

            moves = board.getMoves('B');
            for (Move move: moves) {
                Board oldBoard = board.clone();

                board.doMove(move);
                Move currMove = getBestMove(depth - 1, alpha, beta);

                board = oldBoard;

                if (depth == maxDepth) {
                    System.out.print('#');
                }

                if (bestMove == null || currMove.evaluation < bestMove.evaluation) {
                    bestMove = move;
                    bestMove.evaluation = currMove.evaluation;
                }
                if (bestMove.evaluation < alpha + 1) {
                    break;
                } else if (bestMove.evaluation < beta) {
                    beta = bestMove.evaluation;
                }
            }
        }

        if (depth == maxDepth)
            System.out.println();

        if (!positions.containsKey(boardCode) || positions.get(boardCode)[1] < depth) {
            positions.put(boardCode, new int[]{bestMove.evaluation, depth});
        }

        return bestMove;
    }

    private int evaluateBoard() {
        int evaluation = board.evaluation;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                if (board.getPiece(file, rank) == 'p') {
                    evaluation -= Constants.blackPawnValues[rank][file];
                } else if (board.getPiece(file, rank) == 'P') {
                    evaluation += Constants.whitePawnValues[rank][file];
                } else if (board.getPiece(file, rank) == 'n') {
                    evaluation -= Constants.blackKnightValues[rank][file];
                } else if (board.getPiece(file, rank) == 'N') {
                    evaluation += Constants.whiteKnightValues[rank][file];
                } else if (board.getPiece(file, rank) == 'b') {
                    evaluation -= Constants.blackBishopValues[rank][file];
                } else if (board.getPiece(file, rank) == 'B') {
                    evaluation += Constants.whiteBishopValues[rank][file];
                } else if (board.getPiece(file, rank) == 'r') {
                    evaluation -= Constants.blackRookValues[rank][file];
                } else if (board.getPiece(file, rank) == 'R') {
                    evaluation += Constants.whiteRookValues[rank][file];
                } else if (board.getPiece(file, rank) == 'q') {
                    evaluation -= Constants.blackQueenValues[rank][file];
                } else if (board.getPiece(file, rank) == 'Q') {
                    evaluation += Constants.whiteQueenValues[rank][file];
                } else if (board.getPiece(file, rank) == 'k') {
                    evaluation -= Constants.blackKingValues[rank][file];
                } else if (board.getPiece(file, rank) == 'K') {
                    evaluation += Constants.whiteKingValues[rank][file];
                }
            }
        }
        return evaluation;
    }
}
