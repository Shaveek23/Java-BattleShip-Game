package GameLogic.Player;

import GameLogic.Board;
import GameLogic.Mode;
import GameLogic.Ships.Ship;
import GameLogic.Ships.ShipsParameters;
import Application.Models.Fields;

import java.awt.*;
import java.util.Random;

public class HumanPlayer extends Player{

    public HumanPlayer(Mode mode) {
        super(new Board(mode.getWidth(), mode.getHeight()), mode, new Ship[0]);
        int shipsCount = 0;
        for (
                ShipsParameters params : mode.getShipsParameters()) {
            shipsCount += params.getCount();
        }
        ships = new Ship[shipsCount];
        placeShips();
    }

    @Override
    public void placeShip(Ship ship, int counter) {
        board.placeShip(ship);
        ships[counter] = ship;
    }

    @Override
    public boolean shoot(Point point) {
        return board.shoot(point);
    }

    @Override
    public Fields getFieldsModel() {
        return this.board.getFieldsModel();
    }

    public void placeShips() {
        while (!randomizeShips()); //try placing ships until final ships configuration is correct
        board.zipFields(); //mark all fields as not hit yet
    }

    //place ships in random position
    //in case of bad configuration (not all ships were placed and it's not possible to add any new ship) returns false
    private boolean randomizeShips() {
        int counter = 0;
        for (ShipsParameters type: mode.getShipsParameters()) {
            for (int i = 0; i < type.getCount(); i++) {
                Ship ship = randomizeShip(type.getSize());
                if (ship == null) {
                    board = new Board(board.getWidth(), board.getHeight());
                    ships = new Ship[ships.length];
                    return false;
                }
                placeShip(ship, counter);
                counter++;
            }
        }

        return true;
    }

    private Ship randomizeShip(int size) {
        int sentinel = 0;
        Random random = new Random();
        boolean horizontal;
        int startX;
        int startY;
        int diff = board.getHeight() - size;
        do {
            sentinel++;
            horizontal = random.nextBoolean();

            if (diff <= 0) {
                startX = horizontal ? 0 : random.nextInt(board.getWidth());
                startY = horizontal ? random.nextInt(board.getHeight()) : 0;
            }
            else {
                startX = horizontal ? random.nextInt(diff) : random.nextInt(board.getHeight());
                startY = horizontal ? random.nextInt(board.getWidth()) : random.nextInt(diff);
            }


            if (sentinel > 100)
                return null;

        } while (!board.isPossibleToPlaceShip(size, horizontal, startX, startY));

        return new Ship(size, horizontal, new Point(startX, startY));
    }
}
