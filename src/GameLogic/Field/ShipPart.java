package GameLogic.Field;

import GameLogic.Ships.Ship;

import java.awt.*;

public class ShipPart implements IField {

    private int partX;
    private int partY;
    private boolean isDestroyed;
    private Ship ship;

    private ShipPart next;

    public void createShipPart(Ship ship, int x, int y, ShipPart next)
    {
        partX = x;
        partY = y;
        this.next = next;
        this.isDestroyed = false;
        this.ship = ship;
    }

    public ShipPart getNext() {
        return this.next;
    }

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
