package GameLogic.Field;

import GameLogic.Ships.Ship;

import java.awt.*;

public class ShipPart implements IField {

    private int partX;
    private int partY;
    private boolean isDestroyed;
    private Ship ship;

    private ShipPart next;
    private ShipPart prev;

    public void createShipPart(Ship ship, int x, int y, ShipPart next, ShipPart prev)
    {
        partX = x;
        partY = y;
        this.next = next;
        this.prev = prev;
        this.isDestroyed = false;
        this.ship = ship;
    }

    public boolean isBeginning() {
        return prev == null;
    }

    public boolean isEnd() {
        return next == null;
    }

    public ShipPart getNext() {
        return this.next;
    }

    public ShipPart getPrev() { return this.prev; }

    public Ship getShip() { return this.ship; }

    @Override
    public int getX() {
        return partX;
    }

    @Override
    public int getY() {
        return partY;
    }

    @Override
    public Point getPoint() {
        return new Point(getX(), getY());
    }

    @Override
    public void hit() {
        this.isDestroyed = true;
    }

    @Override
    public boolean isHit() {
        return isDestroyed;
    }

    @Override
    public boolean isShip() {
        return true;
    }


}
