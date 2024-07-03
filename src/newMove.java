public class newMove {
    public int oldFile;
    public int oldRank;
    public int newFile;
    public int newRank;
    public double evaluation;

    public newMove(int oldFile, int oldRank, int newFile, int newRank) {
        this.oldFile = oldFile;
        this.oldRank = oldRank;
        this.newFile = newFile;
        this.newRank = newRank;
    }
}
