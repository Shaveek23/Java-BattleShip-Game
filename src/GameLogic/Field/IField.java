package GameLogic.Field;


import java.awt.*;

public interface IField {

    int getX();
    int getY();
    Point getPoint();

    boolean isHit();
    void hit();

    boolean isShip();
}
