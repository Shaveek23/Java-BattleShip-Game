package Application.Display;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import Application.Models.Field;
import Application.Models.Fields;


public class DisplayBoard extends Parent {
    private VBox rows = new VBox();
    private int width;
    private int height;

    public DisplayBoard(int width, int height, Fields model, EventHandler<? super MouseEvent> handler) {
        this.width = width;
        this.height = height;
        for (int y = 0; y < height; y++) {
            HBox row = new HBox();
            for (int x = 0; x < width; x++) {
                Cell c = new Cell(x, y);
                displayCell(c, model.get(x, y));
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

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell c = getCell(x, y);
                displayCell(c, model.get(x, y));
            }
        }
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
