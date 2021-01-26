package GameLogic.Ships;

import GameLogic.Field.ShipPart;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Ship implements Iterable<ShipPart> {
    private int size;
    private boolean horizontal;
    private ShipPart beginning;
    private ShipPart end;

    //assuming that beginning and end points enable correct ship creation
    public Ship(int size, boolean horizontal, Point beginning)
    {
        this.size = size;
        this.horizontal = horizontal;
        createShip(size, horizontal, beginning);
    }

    private void createShip(int size, boolean horizontal, Point beginning)
    {
        ArrayList<ShipPart> shipParts = new ArrayList<>(size);
        int dx = horizontal ? 1 : 0;
        int dy = horizontal ? 0 : 1;
        ShipPart prev;
        ShipPart next;

        for (int i = 0; i < size; i++)
            shipParts.add(new ShipPart());

        for (int i = 0; i < size; i++) {
            prev = i == 0 ? null : shipParts.get(i - 1);
            next = i == (size - 1) ? null : shipParts.get((i + 1));
            ShipPart part = shipParts.get(i);
            part.createShipPart(this,beginning.x + i * dx, beginning.y + i * dy, next, prev);
        }

        this.beginning = shipParts.get(0);
        this.end = shipParts.get(shipParts.size() - 1);
    }

    public Point getBeginningCoordinates() {
        return new Point(this.beginning.getX(), this.beginning.getY());
    }

    public boolean isHorizontal() { return this.horizontal; }

    public int getSize() { return this.size; }

    @Override
    public Iterator<ShipPart> iterator() {
        return new ShipIterator(this.beginning);
    }

    public boolean isDestroyed() {
        for (ShipPart part: this) {
            if (!part.isHit())
                return false;
        }
        return true;
    }
}
