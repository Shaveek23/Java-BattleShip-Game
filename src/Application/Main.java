package Application;

import GameLogic.*;
import GameLogic.Ships.ShipsParameters;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Application.Display.DisplayController;


public class Main extends Application {

    private int boardWidth = 8;
    private int boardHeight = 8;
    private ShipsParameters[] ships = mockShipsParameters();

    @Override
    public void start(Stage primaryStage) {
        Mode mode = initializeGameMode();
        DisplayController displayController = initializeGame(mode);
        Scene scene = new Scene(displayController.createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Mode initializeGameMode() {
        return new Mode(boardWidth, boardHeight, ships);
    }

    private DisplayController initializeGame(Mode mode) {

        Game game = new Game(mode);
        return new DisplayController(game);
    }


    public static void main(String[] args) {
        launch(args);
    }

    private ShipsParameters[] mockShipsParameters() {
        return new ShipsParameters[] {
                new ShipsParameters( 1, 4),
                new ShipsParameters( 3, 3),
                new ShipsParameters(4, 2),
        };
    }
}
