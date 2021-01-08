package Player;

import Game.Board;
import Game.Mode;
import Game.Ships.Ship;
import Game.Ships.ShipsParameters;

import java.awt.*;
import java.util.Random;

public class HumanPlayer extends Player{

    public HumanPlayer() {
        super(new Board(Mode.getWidth(), Mode.getHeight()), new Ship[0]);
        int shipsCount = 0;
        for (
                ShipsParameters params : Mode.getShipsParameters()) {
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

    public void placeShips() {
        while (!randomizeShips());
        board.zipFields();
    }

    private boolean randomizeShips() {
        int counter = 0;
        for (ShipsParameters type: Mode.getShipsParameters()) {
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
