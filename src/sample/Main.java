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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;


public class Main extends Application {

    private static DisplayBoard enemyBoard;
    private static DisplayBoard playerBoard;

    private static ComputerPlayer computer;
    private static ComputerPlayer human;

    private Scene scene;

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

//    private static void computerBoardTest() {
//        int width = 8;
//        int height = 8;
//        new Mode(width, height, mockShipsParameters());
//        ComputerPlayer computer = new ComputerPlayer();
//        computer.placeShips();
//        Scanner scan = new Scanner(System.in);
//        while (!computer.isLoser()) {
////            System.out.println("Strzelaj! Podaj wspolrzedne punktu:");
////            int x = scan.nextInt();
////            int y = scan.nextInt();
////
////            if (computer.board.shoot(new Point(x, y))) {
////                System.out.println("Trafiłeś!");
////            } else {
////                System.out.println("Pudło!");
////            }
////            printBoard(computer.board);
////            System.out.print("--------------------------------------------------");
////            System.out.println();
//
//            if (computer.shoot())
//                System.out.println("Trafiono!");
//            else
//                System.out.println("Pudło!");
//
//
//            printBoard(computer.board);
//            System.out.print("--------------------------------------------------");
//            System.out.println();
//        }

    //}


    private boolean running = false;


    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        root.setRight(new Text("RIGHT SIDEBAR - CONTROLS"));



        enemyBoard = new DisplayBoard(human.getFieldsModel(), event -> {

            if (Game.isPlayerPlacingShips || Game.isEnded)
                return;

            Player currentPlayer;
            DisplayBoard currentDisplayBoard;
            var e = enemyBoard.getCell(0,0).getOnMouseClicked();

            Point target;
            if (Game.isComputersTurn) {
                target = human.getNextTarget();
                currentPlayer = human;
                currentDisplayBoard = playerBoard;

            } else {
                Cell c = (Cell) event.getSource();
                Cell cell = enemyBoard.getCell(c.x, c.y);
                if (cell.state != Field.UNKNOWN)
                    return;
                e = enemyBoard.getCell(c.x, c.y).getOnMouseClicked();
                target = new Point(cell.x, cell.y);
                currentPlayer = computer;
                currentDisplayBoard = enemyBoard;
            }


            boolean isHit = currentPlayer.shoot(target);

            currentDisplayBoard.refreshView(currentPlayer.getFieldsModel());


            if (currentPlayer.isLoser())
            {
                System.out.println("YOU WIN");
                Game.isEnded = true;
            }
            if (!isHit) {
                Game.isComputersTurn = !Game.isComputersTurn;
            }

            if (Game.isComputersTurn) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException interruptedException) {
//                    interruptedException.printStackTrace();
//                }
                e.handle(event);
            }

        });

        playerBoard = new DisplayBoard(computer.getFieldsModel(), event -> {

            Cell cell = (Cell) event.getSource();

            if (Game.isPlayerPlacingShips) {
                int nextShipSize = Mode.nextShipSize();
                if (nextShipSize > 0) {
                    if (human.board.isPossibleToPlaceShip(nextShipSize, (event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                        Ship newShip = new Ship(nextShipSize, (event.getButton() == MouseButton.PRIMARY), new Point(cell.x, cell.y));
                        human.placeShip(newShip, Mode.getCurrentShipNo());
                        playerBoard.refreshView(human.getFieldsModel());
                    } else {
                        Mode.bringBackSize();
                    }
                } else {
                    Game.isPlayerPlacingShips = false;
                    playerBoard.refreshView(human.board.getFieldsModel());
                    human.board.zipFields();
                    Game.isPreview = false;
                }
                playerBoard.refreshView(human.board.getFieldsModel());
            }
        });

        VBox vbox = new VBox(50, enemyBoard, playerBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    private Fields getMockedFields() {
        Fields fields = new Fields();
        for (int i = 0; i < Mode.getHeight(); i++) {
            for (int j = 0; j < Mode.getWidth(); j++) {
                fields.fields[i][j] = Field.UNKNOWN;
            }
        }

        fields.fields[0][0] = Field.DESTROYED;

        fields.fields[5][5] = Field.MISS;

        return fields;
    }

    private Fields getMockedFields2() {
        Fields fields = new Fields();
        for (int i = 0; i < Mode.getHeight(); i++) {
            for (int j = 0; j < Mode.getWidth(); j++) {
                fields.fields[i][j] = Field.UNKNOWN;
            }
        }

        fields.fields[4][0] = Field.DESTROYED;

        fields.fields[5][6] = Field.MISS;

        return fields;
    }

}
