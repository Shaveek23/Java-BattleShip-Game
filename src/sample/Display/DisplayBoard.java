package sample.Display;

import Game.Game;
import Game.Mode;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sample.Models.Field;
import sample.Models.Fields;


public class DisplayBoard extends Parent {
    private VBox rows = new VBox();
    private Fields model;

    public DisplayBoard(Fields model, EventHandler<? super MouseEvent> handler) {
        this.model = model;
        for (int y = 0; y < Mode.getHeight(); y++) {
            HBox row = new HBox();
            for (int x = 0; x < Mode.getWidth(); x++) {
                Cell c = new Cell(x, y);
                if (model.fields[y][x] == Field.MISS ) {
                    c.setFill(Color.BLUE);
                    c.setState(Field.MISS);
                } else if (model.fields[y][x] == Field.DESTROYED) {
                    c.setFill(Color.RED);
                    c.setState(Field.DESTROYED);
                } else {
                    c.setFill(Color.LIGHTGRAY);
                    c.setState(Field.UNKNOWN);
                }
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    public void refreshView(Fields model) {
        if (model == null)
            return;

        for (int y = 0; y < Mode.getHeight(); y++) {
            for (int x = 0; x < Mode.getWidth(); x++) {
                Cell c = getCell(x, y);
                if (model.fields[y][x] == Field.MISS) {
                    c.setState(Field.MISS);
                    c.setFill(Color.BLUE);
                } else if (model.fields[y][x] == Field.DESTROYED) {
                    c.setFill(Color.RED);
                    c.setState(Field.DESTROYED);
                } else if (Game.isPreview && model.fields[y][x] == Field.PREVIEW) {
                    c.setFill(Color.GREEN);
                    c.setState(Field.PREVIEW);
                } else {
                    c.setFill(Color.LIGHTGRAY);
                    c.setState(Field.UNKNOWN);
                }
            }
        }
        this.model = model;
        return;
    }
}
