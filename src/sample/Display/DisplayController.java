package sample.Display;

import Game.Game;
import Game.*;
import Game.Ships.Ship;
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
import sample.Models.Field;

import java.awt.*;

public class DisplayController {

    private static DisplayBoard computerPlayerBoard;
    private static DisplayBoard humanPlayerBoard;
    private static DisplayBoard currentDisplayBoard;

    private static Label humanLabel;
    private static Label computerLabel;

    private static Label humanInfoLabel;
    private static Label computerInfoLabel;
    private static Label currentInfoLabel;

    private static Label shipsLabel;


    public DisplayController() {
        initializeComputerBoard();
        initializePlayerBoard();
        initializeLabels();
    }

    public static Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);
        VBox vbox = new VBox(50, humanLabel, humanPlayerBoard, humanInfoLabel, new Label(), computerLabel, computerPlayerBoard, computerInfoLabel, shipsLabel);
        vbox.setSpacing(12);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);
        return root;
    }

    private static void initializeLabels() {
        // initialize enemyLabel
        humanLabel = new Label("Enemy's Board:");
        humanLabel.setFont(new javafx.scene.text.Font("Verdana", 20));

        humanInfoLabel = new Label("");

        //initialize player's labels
        computerLabel = new Label("Your Board:");
        computerLabel.setFont(new javafx.scene.text.Font("Verdana", 20));

        int size = Mode.nextShipSize();
        Mode.bringBackSize();
        computerInfoLabel = new Label("Place your ships on this board first! Horizontal - LMB, Vertical - RMB\nNext size: " + size);
        computerInfoLabel.setTextFill(Color.RED);

        // initialize ships label
        shipsLabel = new Label(Mode.getShips());
        shipsLabel.setFont((new Font("Verdana", 18)));
        shipsLabel.setTextFill(Color.BLUE);
    }

    private static void initializePlayerBoard() {
        EventHandler<MouseEvent> computerBoardHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Game.isPlayerPlacingShips || Game.isEnded)
                    return;

                Point target;

                if (Game.isComputersTurn) {
                    target = Game.computerPlayer.getNextTarget();
                    Game.currentPlayer = Game.computerPlayer;
                    currentDisplayBoard = computerPlayerBoard;
                    currentInfoLabel = computerInfoLabel;

                } else {
                    Cell c = (Cell) event.getSource();
                    Cell cell = humanPlayerBoard.getCell(c.x, c.y);
                    if (cell.getState() != Field.UNKNOWN)
                        return;
                    target = new Point(cell.x, cell.y);
                    Game.currentPlayer = Game.humanPlayer;
                    currentDisplayBoard = humanPlayerBoard;
                    currentInfoLabel = humanInfoLabel;
                }


                boolean isHit = Game.currentPlayer.shoot(target);
                currentDisplayBoard.refreshView(Game.currentPlayer.getFieldsModel());


                if (Game.currentPlayer.isLoser())
                {
                    currentInfoLabel.setText("WIN!!!");
                    Game.isEnded = true;
                    return;
                }

                String result;
                if (!isHit) {
                    result = "MISS!!!";
                    if (!Game.isComputersTurn) {
                        computerInfoLabel.setText("");
                    }
                    Game.isComputersTurn = !Game.isComputersTurn;
                } else {
                    result = "HIT!!!";
                }

                if (Game.isComputersTurn) {
                    computerInfoLabel.setText(result + " " + computerInfoLabel.getText());
                    this.handle(event);
                } else {
                    humanInfoLabel.setText(result);
                }
            }
        };

        humanPlayerBoard = new DisplayBoard(Game.humanPlayer.getFieldsModel(), computerBoardHandler);
    }

    private static void initializeComputerBoard() {
        computerPlayerBoard = new DisplayBoard(Game.computerPlayer.getFieldsModel(), event -> {

            Cell cell = (Cell) event.getSource();

            if (Game.isPlayerPlacingShips) {
                int nextShipSize = Mode.nextShipSize();
                if (Game.computerPlayer.isPossibleToPlaceShip(nextShipSize, (event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                    Ship newShip = new Ship(nextShipSize, (event.getButton() == MouseButton.PRIMARY), new Point(cell.x, cell.y));
                    Game.computerPlayer.placeShip(newShip, Mode.getCurrentShipNo());
                    int nextNextSize = Mode.nextShipSize();
                    computerInfoLabel.setText("Place your ships on this board first! Horizontal - LMB, Vertical - RMB\nNext size: " + nextNextSize);

                    if (nextNextSize == 0) {
                        Game.isPlayerPlacingShips = false;
                        Game.computerPlayer.zipFields();
                        Game.isPreview = false;
                        computerInfoLabel.setText("");
                        humanInfoLabel.setText("It's your turn! Shoot");
                        humanInfoLabel.setTextFill(Color.RED);
                    }
                    Mode.bringBackSize();

                } else {
                    Mode.bringBackSize();
                    computerInfoLabel.setText("Wrong ship position!\n Try again!");
                }
                computerPlayerBoard.refreshView(Game.computerPlayer.getFieldsModel());
            }
        });
    }
}
