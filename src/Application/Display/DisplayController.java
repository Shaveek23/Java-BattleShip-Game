package Application.Display;

import GameLogic.Game;
import GameLogic.Ships.Ship;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import Application.Models.Field;

import java.awt.*;

public class DisplayController {

    private DisplayBoard computerPlayerBoard;
    private DisplayBoard humanPlayerBoard;
    private DisplayBoard currentDisplayBoard;

    private Label humanLabel;
    private Label computerLabel;

    private Label humanInfoLabel;
    private Label computerInfoLabel;
    private Label currentInfoLabel;

    private Label shipsLabel;

    private Game game;

    public DisplayController(Game game) {
        this.game = game;
        initializeComputerBoard();
        initializePlayerBoard();
        initializeLabels();
    }

    public Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        VBox vbox = new VBox(50, humanLabel, humanPlayerBoard, humanInfoLabel, new Label(), computerLabel, computerPlayerBoard, computerInfoLabel, shipsLabel);
        vbox.setSpacing(12);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);
        return root;
    }

    private void initializeLabels() {
        // initialize enemyLabel
        humanLabel = new Label("Enemy's Board:");
        humanLabel.setFont(new javafx.scene.text.Font("Verdana", 20));

        humanInfoLabel = new Label("");

        //initialize player's labels
        computerLabel = new Label("Your Board:");
        computerLabel.setFont(new javafx.scene.text.Font("Verdana", 20));

        int size = game.getGameMode().nextShipSize();
        game.getGameMode().bringBackSize();
        computerInfoLabel = new Label("Place your ships on this board first! Horizontal - LMB, Vertical - RMB\nNext size: " + size);
        computerInfoLabel.setTextFill(Color.RED);

        // initialize ships label
        shipsLabel = new Label(game.getGameMode().getShips());
        shipsLabel.setFont((new Font("Verdana", 18)));
        shipsLabel.setTextFill(Color.BLUE);
    }

    private void initializePlayerBoard() {
        EventHandler<MouseEvent> computerBoardHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (game.isPlayerPlacingShips() || game.isEnded())
                    return;

                Point target;

                if (game.isComputersTurn()) {
                    target = game.getComputerPlayer().getNextTarget();
                    game.setCurrentPlayer(game.getComputerPlayer());
                    currentDisplayBoard = computerPlayerBoard;
                    currentInfoLabel = computerInfoLabel;

                } else {
                    Cell c = (Cell) event.getSource();
                    Point p = c.get();
                    Cell cell = humanPlayerBoard.getCell(p.x, p.y);
                    if (cell.getState() != Field.UNKNOWN)
                        return;
                    target = cell.get();
                    game.setCurrentPlayer(game.getHumanPlayer());
                    currentDisplayBoard = humanPlayerBoard;
                    currentInfoLabel = humanInfoLabel;
                }


                boolean isHit = game.getCurrentPlayer().shoot(target);
                currentDisplayBoard.refreshView(game.getCurrentPlayer().getFieldsModel());


                if (game.getCurrentPlayer().isLoser())
                {
                    currentInfoLabel.setText("WIN!!!");
                    game.setIsEnded(true);
                    return;
                }

                String result;
                if (!isHit) {
                    result = "MISS!!!";
                    if (!game.isComputersTurn()) {
                        computerInfoLabel.setText("");
                    }
                    game.setIsComputersTurn(!game.isComputersTurn());
                } else {
                    result = "HIT!!!";
                }

                if (game.isComputersTurn()) {
                    computerInfoLabel.setText(result + " " + computerInfoLabel.getText());
                    this.handle(event);
                } else {
                    humanInfoLabel.setText(result);
                }
            }
        };

        humanPlayerBoard = new DisplayBoard(
                game.getGameMode().getWidth(),
                game.getGameMode().getHeight(),
                game.getHumanPlayer().getFieldsModel(),
                computerBoardHandler);
    }

    private void initializeComputerBoard() {
        computerPlayerBoard = new DisplayBoard(
                game.getGameMode().getWidth(),
                game.getGameMode().getHeight(),
                game.getComputerPlayer().getFieldsModel(),
                event ->
        {

            Cell cell = (Cell) event.getSource();

            if (game.isPlayerPlacingShips()) {
                int nextShipSize = game.getGameMode().nextShipSize();
                if (game.getComputerPlayer().isPossibleToPlaceShip(nextShipSize, (event.getButton() == MouseButton.PRIMARY), cell.get().x, cell.get().y)) {
                    Ship newShip = new Ship(nextShipSize, (event.getButton() == MouseButton.PRIMARY), cell.get());
                    game.getComputerPlayer().placeShip(newShip, game.getGameMode().getCurrentShipNo());
                    int nextNextSize = game.getGameMode().nextShipSize();
                    computerInfoLabel.setText("Place your ships on this board first! Horizontal - LMB, Vertical - RMB\nNext size: " + nextNextSize);

                    if (nextNextSize == 0) {
                        game.setIsPlayerPlacingShips(false);
                        game.getComputerPlayer().zipFields();
                        computerInfoLabel.setText("");
                        humanInfoLabel.setText("It's your turn! Shoot");
                        humanInfoLabel.setTextFill(Color.RED);
                    }
                    game.getGameMode().bringBackSize();

                } else {
                    game.getGameMode().bringBackSize();
                    computerInfoLabel.setText("Wrong ship position!\n Try again!");
                }
                computerPlayerBoard.refreshView(game.getComputerPlayer().getFieldsModel());
            }
        });
    }
}
