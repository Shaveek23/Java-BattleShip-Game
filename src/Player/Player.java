package Player;

import Game.Board;
import Game.Ships.Ship;
import sample.Models.Fields;

import java.awt.*;

public abstract class Player {

    public Board board; // change to protected
    public Ship[] ships; //change to protected

    protected Player(Board board, Ship[] ships) {
        this.board = board;
        this.ships = ships;
    }

    public abstract void placeShip(Ship ship, int counter);
    public abstract boolean shoot(Point point);

    public boolean isLoser() {
        if (ships == null)
            return false;

        for (var ship : ships) {
            if (!ship.isDestroyed())
                return false;
        }
        return true;
    }

    public boolean isShipDestroyed(Point point) {
       for (var ship : ships) {
           for (var shipPart : ship) {
               if (shipPart.getX() == point.x && shipPart.getY() == point.y) {
                   return ship.isDestroyed();
               }
           }
       }
       return false;
    }

    public abstract Fields getFieldsModel();

}
