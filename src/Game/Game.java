package Game;

import Player.ComputerPlayer.ComputerPlayer;
import Player.Player;
import Player.HumanPlayer;

public class Game {

    public static boolean isPlayerPlacingShips = true;

    public static boolean isEnded = false;

    public static boolean isComputersTurn = false;

    public static boolean isPreview = true;


    public static HumanPlayer humanPlayer;
    public static ComputerPlayer computerPlayer;
    public static Player currentPlayer;


    public Game() {
        humanPlayer = new HumanPlayer();
        computerPlayer = new ComputerPlayer();
    }


}
