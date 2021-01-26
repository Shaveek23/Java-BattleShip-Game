package sample;

import Game.*;
import Game.Ships.Ship;
import Game.Ships.ShipsParameters;
import Player.ComputerPlayer.ComputerPlayer;
import Player.Player;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Display.Cell;
import sample.Display.DisplayBoard;
import sample.Display.DisplayController;
import sample.Models.Field;
import sample.Models.Fields;

import java.awt.*;


public class Main extends Application {

    private static int boardWidth = 8;
    private static int boardHeight = 8;
    private static ShipsParameters[] ships = mockShipsParameters();

    @Override
    public void start(Stage primaryStage) throws Exception{
        initializeGame();
        Scene scene = new Scene(DisplayController.createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void initializeGame() {
        Mode mode = new Mode(boardWidth, boardHeight, ships);
        Game game = new Game(mode);
        new DisplayController(game);
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



/*
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
*/
}
