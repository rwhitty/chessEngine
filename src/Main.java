import java.util.*;
public class Main {
    public static void main(String[] args) {
        Engine engine = new Engine();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the file (lowercase) of the piece you want to move:");
            String oldFile = scanner.nextLine();
            System.out.println("Enter the rank of the piece you want to move:");
            String oldRank = scanner.nextLine();
            System.out.println("Enter the file (lowercase) you'd like this piece to move to:");
            String newFile = scanner.nextLine();
            System.out.println("Enter the rank you'd like this piece to move to:");
            String newRank = scanner.nextLine();
            engine.processMove(oldFile + oldRank + newFile + newRank);
            String engineMove = engine.makeMove();
            System.out.println("Engine's move: " + engineMove);
        }
    }
}