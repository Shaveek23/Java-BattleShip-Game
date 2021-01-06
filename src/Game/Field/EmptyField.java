package Game.Field;

import java.awt.*;

public class EmptyField implements IField {
    private int fieldX;
    private int fieldY;

    private boolean isHit;
    private boolean isNeighbour;

    public EmptyField(int x, int y) {
        fieldX = x;
        fieldY = y;
        isHit = false;
        isNeighbour = false;
    }


    @Override
    public int getX() {
        return fieldX;
    }

    @Override
    public int getY() {
        return fieldY;
    }

    @Override
    public Point getPoint() {
        return new Point(getX(), getY());
    }

    @Override
    public boolean isHit() {
        return isHit;
    }

    @Override
    public void hit() {
        isHit = true;
    }

    @Override
    public boolean isShip() {
        return false;
    }


}
