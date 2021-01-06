package Game.Field;

import Game.Game;

import java.awt.*;

public interface IField {

    public int getX();
    public int getY();
    public Point getPoint();

    public boolean isHit();
    public void hit();

    public boolean isShip();
}
