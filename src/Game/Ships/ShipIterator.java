package Game.Ships;

import Game.Field.ShipPart;

import java.util.Iterator;

public class ShipIterator implements Iterator<ShipPart> {

    private ShipPart current;

    public ShipIterator(ShipPart beginning)
    {
        current = beginning;
    }

    @Override
    public boolean hasNext() {
       return current != null;
    }

    @Override
    public ShipPart next() {
        ShipPart ret = current;
        current = current.getNext();
        return ret;
    }
}
