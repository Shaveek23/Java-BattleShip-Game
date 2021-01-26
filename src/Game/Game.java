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

    private static Mode mode;

    public Game(Mode mode) {
        this.mode = mode;
        humanPlayer = new HumanPlayer(mode);
        computerPlayer = new ComputerPlayer(mode);
    }

    public static Mode getGameMode() {
        return mode;
    }


}
