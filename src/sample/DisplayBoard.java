package sample;

import Game.Board;
import Game.Game;
import Game.Mode;
import Game.Ships.Ship;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;


public class DisplayBoard extends Parent {
    private VBox rows = new VBox();
    private Fields model;
    //private static Fields preview = new Fields();

    public DisplayBoard(Fields model, EventHandler<? super MouseEvent> handler) {
        this.model = model;
        for (int y = 0; y < Mode.getHeight(); y++) {
            HBox row = new HBox();
            for (int x = 0; x < Mode.getWidth(); x++) {
                Cell c = new Cell(x, y, this);
                if (model.fields[y][x] == Field.MISS ) {
                    c.setFill(Color.BLUE);
                    c.state = Field.MISS;
                } else if (model.fields[y][x] == Field.DESTROYED) {
                    c.setFill(Color.RED);
                    c.state = Field.DESTROYED;
                } else {
                    c.setFill(Color.LIGHTGRAY);
                    c.state = Field.UNKNOWN;
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
                    c.state = Field.MISS;
                    c.setFill(Color.BLUE);
                } else if (model.fields[y][x] == Field.DESTROYED) {
                    c.setFill(Color.RED);
                    c.state = Field.DESTROYED;
                } else if (Game.isPreview && model.fields[y][x] == Field.PREVIEW) {
                    c.setFill(Color.GREEN);
                    c.state = Field.PREVIEW;
                } else {
                    c.setFill(Color.LIGHTGRAY);
                    c.state = Field.UNKNOWN;
                }
            }
        }
        this.model = model;
        return;
    }

    public void addShipPreview(Ship ship) {
        int dx = ship.isHorizontal() ? 1 : 0;
        int dy = ship.isHorizontal() ? 0 : 1;
        Point start = ship.getBeginningCoordinates();
        for (int i = 0; i < ship.getSize(); i++) {
            model.fields[i * dy + start.y][i * dx + start.x] = Field.PREVIEW;
        }
    }

    public Fields getPreview() {
        return this.model;
    }

    public Queue<Fields> animation = new LinkedList<Fields>();

    public void addAnimationFrame(Fields viewModel) {
        animation.add(viewModel);
    }

    public void showAnimationFrame() {
        var frame = animation.poll();
        this.refreshView(frame);
    }


}
