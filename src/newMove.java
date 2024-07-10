public class newMove {
    public int oldFile;
    public int oldRank;
    public int newFile;
    public int newRank;
    public double evaluation;

    // specialType: 'k' for king-side castle, 'q' for queen-side castle, 'e' for en-passant, 'p' for promotion
    public newMove(int oldFile, int oldRank, int newFile, int newRank, char specialType) {
        this.oldFile = oldFile;
        this.oldRank = oldRank;
        this.newFile = newFile;
        this.newRank = newRank;
    }
}
