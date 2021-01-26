package Application.Display;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Application.Models.Field;

import java.awt.*;

public class Cell extends Rectangle {
    private int x;
    private int y;
    private Field state;

    public Cell(int x, int y) {
        super(30, 30);
        this.x = x;
        this.y = y;
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);
    }

    public Field getState() {
        return this.state;
    }

    public void setState(Field state) {
        this.state = state;
    }

    public Point get() {
        return new Point(x,y);
    }

}
