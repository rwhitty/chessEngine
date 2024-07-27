public class Move {
    public int oldFile;
    public int oldRank;
    public int newFile;
    public int newRank;
    public char specialType;
    public int evaluation;

    // specialType: 'k' for king-side castle, 'q' for queen-side castle, 'e' for en-passant, 'p' for promotion
    public Move(int oldFile, int oldRank, int newFile, int newRank, char specialType) {
        this.oldFile = oldFile;
        this.oldRank = oldRank;
        this.newFile = newFile;
        this.newRank = newRank;
        this.specialType = specialType;
        this.evaluation = 0;
    }

    public int getPriority() {
        return evaluation;
    }
}
