
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Awari or Oware is an Ashanti abstract strategy game among the Mancala family of board games.
 * Read more about it here: https://en.wikipedia.org/wiki/Oware
 * @author Shubham Aggarwal
 *
 */
public class Awari {

    public static GUI gui; //Class handling the graphical interface.

    public static final boolean VERBOSE = true;

    public static void main(String[] args) throws Exception {
        String p1, p2;
        BoardState initial;

        if (args.length >= 3) { //P1 = Player one, P2= Player two. For either of them, we can have AI or human
            p1 = args[0];
            p2 = args[1];
            initial = loadFile(args[2]); //used to load an initial configuration. (Made to test the AI)
                                         //By default initial instantiates all the pots with 4 stones.
        } else { //by default two human players
            p1 = "HumanPlayer";
            p2 = "HumanPlayer";
            initial = null;
        }


        Player player1 = (Player) Class.forName(p1).newInstance();
        Player player2 = (Player) Class.forName(p2).newInstance();

        if (p1.endsWith("AI"))  ((AI)player1).setMaxDepth(Integer.parseInt(args[3]));
        if (p2.endsWith("AI"))  ((AI)player2).setMaxDepth(Integer.parseInt(args[3]));

        gui = new GUI("Player 1", "Player 2", initial);

        Match match = new Match(player1, player2, initial);
        int winner = match.play();
        if (winner == 0) {
            gui.textArea.append("Game is a draw");
        } else {
            gui.textArea.append("Winner is " + winner);
        }
    }

    private static BoardState loadFile(String filename) { //Used to instantiate the board setting.
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            int[] score = new int[2];
            int[] house = new int[12];
            score[0] = sc.nextInt();
            score[1] = sc.nextInt();
            for (int i = 0; i < 6; i++) {
                house[11-i] = sc.nextInt(); //Player 1's instantiation
            }
            for (int i = 0; i < 6; i++) {
                house[i] = sc.nextInt(); //Player 2's instantiation
            }
            sc.close();
            return new BoardState(house, score);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
