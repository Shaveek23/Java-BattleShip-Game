package sample.Display;

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
                displayCell(c, model.fields[y][x]);
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
                displayCell(c, model.fields[y][x]);
            }
        }
        this.model = model;
        return;
    }

    private void displayCell(Cell c, Field cellState) {
        if (cellState == Field.MISS) {
            c.setState(Field.MISS);
            c.setFill(Color.BLUE);
        } else if (cellState == Field.DESTROYED) {
            c.setFill(Color.RED);
            c.setState(Field.DESTROYED);
        } else if (cellState == Field.PREVIEW) {
            c.setFill(Color.GREEN);
            c.setState(Field.PREVIEW);
        } else {
            c.setFill(Color.LIGHTGRAY);
            c.setState(Field.UNKNOWN);
        }
    }
}
