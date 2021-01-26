package Game;

import Player.ComputerPlayer.ComputerPlayer;
import Player.Player;
import Player.HumanPlayer;

public class Game {

    private boolean isPlayerPlacingShips = true;

    private boolean isEnded = false;

    private boolean isComputersTurn = false;

    private HumanPlayer humanPlayer;
    private ComputerPlayer computerPlayer;
    private Player currentPlayer;

    private Mode mode;

    public Game(Mode mode) {
        this.mode = mode;
        humanPlayer = new HumanPlayer(mode);
        computerPlayer = new ComputerPlayer(mode);
    }

    public Mode getGameMode() {
        return mode;
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public ComputerPlayer getComputerPlayer() { return computerPlayer; }

    public Player getCurrentPlayer() { return currentPlayer; }

    public boolean isEnded() { return isEnded; }

    public boolean isComputersTurn() { return isComputersTurn; }

    public boolean isPlayerPlacingShips() { return  isPlayerPlacingShips; }


    public void setCurrentPlayer(Player computerPlayer) {
        this.currentPlayer = computerPlayer;
    }

    public void setIsEnded(boolean b) {
        this.isEnded = b;
    }

    public void setIsComputersTurn(boolean b) {
        this.isComputersTurn = b;
    }

    public void setIsPlayerPlacingShips(boolean b) {
        this.isPlayerPlacingShips = b;
    }
}
