package sample;

import Game.*;
import Game.Ships.Ship;
import Game.Ships.ShipsParameters;
import Player.ComputerPlayer.ComputerPlayer;
import Player.Player;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;


public class Main extends Application {

    private static DisplayBoard enemyBoard;
    private static DisplayBoard playerBoard;

    private static ComputerPlayer computer;
    private static ComputerPlayer human;

    private static Label enemyInfoLabel;
    private static Label playerInfoLabel;

    private static Player currentPlayer;
    private static DisplayBoard currentDisplayBoard;
    private static Label currentInfoLabel;

    private Scene scene;

    private Button btn;

    @Override
    public void start(Stage primaryStage) throws Exception{
        int width = 8;
        int height = 8;
        new Mode(width, height, mockShipsParameters());
        computer = new ComputerPlayer();
        computer.placeShips();

        human = new ComputerPlayer();

        scene = new Scene(createContent());
        // toggle the visibility of 'rect' every 500ms
//        AnimationTimer timer =
//                new AnimationTimer() {
//
//                    private long lastToggle;
//
//                    @Override
//                    public void handle(long now) {
//                        if (lastToggle == 0L) {
//                            lastToggle = now;
//                        } else {
//                            long diff = now - lastToggle;
//                            if (diff >= 500_000_000L) { // 500,000,000ns == 500ms
//
//                            }
//                        }
//                    }
//                };
//        timer.start();
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static ShipsParameters[] mockShipsParameters() {
        return new ShipsParameters[] {
                new ShipsParameters( 1, 4),
                new ShipsParameters( 3, 3),
                new ShipsParameters(4, 2),
        };
    }

    private boolean running = false;


    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Game.isPlayerPlacingShips || Game.isEnded)
                    return;

                Point target;
                if (Game.isComputersTurn) {
                    target = human.getNextTarget();
                    currentPlayer = human;
                    currentDisplayBoard = playerBoard;
                    currentInfoLabel = playerInfoLabel;

                } else {
                    Cell c = (Cell) event.getSource();
                    Cell cell = enemyBoard.getCell(c.x, c.y);
                    if (cell.state != Field.UNKNOWN)
                        return;
                    target = new Point(cell.x, cell.y);
                    currentPlayer = computer;
                    currentDisplayBoard = enemyBoard;
                    currentInfoLabel = enemyInfoLabel;
                }


                boolean isHit = currentPlayer.shoot(target);


                currentDisplayBoard.refreshView(currentPlayer.getFieldsModel());


                if (currentPlayer.isLoser())
                {
                    currentInfoLabel.setText("WIN!!!");
                    Game.isEnded = true;
                }

                String result;
                if (!isHit) {
                    result = "MISS!!!";
                    if (!Game.isComputersTurn) {
                        playerInfoLabel.setText("");
                    }
                    Game.isComputersTurn = !Game.isComputersTurn;
                } else {
                    result = "HIT!!!";
                }

                if (Game.isComputersTurn) {

                    playerInfoLabel.setText(result + " " + playerInfoLabel.getText());

                    this.handle(event);
                } else {
                    enemyInfoLabel.setText(result);
                }

            }
        };

        enemyBoard = new DisplayBoard(human.getFieldsModel(), eventHandler);

        playerBoard = new DisplayBoard(computer.getFieldsModel(), event -> {

            Cell cell = (Cell) event.getSource();

            if (Game.isPlayerPlacingShips) {
                int nextShipSize = Mode.nextShipSize();
                if (human.board.isPossibleToPlaceShip(nextShipSize, (event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                    Ship newShip = new Ship(nextShipSize, (event.getButton() == MouseButton.PRIMARY), new Point(cell.x, cell.y));
                    human.placeShip(newShip, Mode.getCurrentShipNo());
                    int nextNextSize = Mode.nextShipSize();
                    playerInfoLabel.setText("Place your ships on this board first! Horizontal - LMB, Vertical - RMB\nNext size: " + nextNextSize);

                    if (nextNextSize == 0) {
                        Game.isPlayerPlacingShips = false;
                        human.board.zipFields();
                        Game.isPreview = false;
                        playerInfoLabel.setText("");
                        enemyInfoLabel.setText("It's your turn! Shoot");
                        enemyInfoLabel.setTextFill(Color.RED);
                    }
                    Mode.bringBackSize();

                } else {
                    Mode.bringBackSize();
                    playerInfoLabel.setText("Wrong ship position!\n Try again!");
                }
                playerBoard.refreshView(human.board.getFieldsModel());
            }
        });
        Label enemyLabel = new Label("Enemy's Board:");
        enemyInfoLabel = new Label("");
        enemyLabel.setFont(new Font("Verdana", 20));
        Label playerLabel = new Label("Your Board:");
        int size = Mode.nextShipSize();
        Mode.bringBackSize();
        playerInfoLabel = new Label("Place your ships on this board first! Horizontal - LMB, Vertical - RMB\nNext size: " + size);
        playerInfoLabel.setTextFill(Color.RED);
        playerLabel.setFont(new Font("Verdana", 20));
        Label shipsLabel = new Label(Mode.getShips());
        shipsLabel.setFont((new Font("Verdana", 18)));
        shipsLabel.setTextFill(Color.BLUE);
        VBox vbox = new VBox(50, enemyLabel, enemyBoard, enemyInfoLabel, new Label(), playerLabel, playerBoard, playerInfoLabel, shipsLabel);
        vbox.setSpacing(12);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }


    private Fields getUnknownFieldsModel() {
        Fields fields = new Fields();
        for (int i = 0; i < Mode.getHeight(); i++) {
            for (int j = 0; j < Mode.getWidth(); j++) {
                fields.fields[i][j] = Field.UNKNOWN;
            }
        }

        return fields;
    }

    private Alert showAlert(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
        return alert;
    }

}
