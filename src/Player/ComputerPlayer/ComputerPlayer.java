package Player.ComputerPlayer;

import Game.*;
import Game.Ships.Ship;
import Game.Ships.ShipsParameters;
import Player.Player;
import sample.Field;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {

    private LastShotTarget lastAccurate = null;
    public ComputerPlayer() {
        super(new Board(Mode.getWidth(), Mode.getHeight()), new Ship[0]);
        int shipsCount = 0;
        for (ShipsParameters params: Mode.getShipsParameters()) {
            shipsCount += params.getCount();
        }
        ships = new Ship[shipsCount];
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

    @Override
    public void placeShip(Ship ship, int counter) {
        board.placeShip(ship);
        ships[counter] = ship;
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

    public Point getNextTarget() {
        Point target;
        if (lastAccurate == null)
            target = board.randomizeTarget();
        else
            target = lastAccurate.next();
        return target;
    }

    @Override
    public boolean shoot(Point target) {

        if (board.shoot(target)) {
            if (lastAccurate == null)
                lastAccurate = new LastShotTarget(target);
            else {
                boolean isDestroyed = lastAccurate.addNextPart(target);
                if (isDestroyed)
                    lastAccurate = null;
            }
            return true;
        }

        return false;
    }


    public class LastShotTarget {

        private Point currentBeginning; // the most right (horizontal) or the most up (vertical)
        private Point currentEnd; // the most left (horizontal) or the most up (vertical)

        private boolean isHorizontal;

        public LastShotTarget(Point beginning) {
            currentBeginning = beginning;
        }

        public Point next() {
            if (currentEnd == null) {
                return Random4Points();
            }
                return Random2Points();
        }

        public boolean addNextPart(Point point) {
            if (isShipDestroyed(point))
                return true;

            if (currentEnd == null) {
                isHorizontal = determineHorizontal(point);
            }

            assignPart(point);

            return false;
        }

        private boolean determineHorizontal(Point point) {
            return point.x != currentBeginning.x;
        }

        private void assignPart(Point point) {
            int pointCord = point.y;
            int begCord = currentBeginning.y;

            if (isHorizontal) {
               pointCord = point.x;
               begCord = currentBeginning.x;
            }
            if (currentEnd == null) {
                if (pointCord < begCord) {
                    currentEnd = currentBeginning;
                    currentBeginning = point;
                } else {
                    currentEnd = point;
                }
            } else {
                if (pointCord < begCord)
                        currentBeginning = point;
                else
                    currentEnd = point;
            }

        }

        private Point Random4Points() {
            ArrayList<Point> targets = new ArrayList<>() {{
                add(new Point( currentBeginning.x - 1, currentBeginning.y));
                add(new Point( currentBeginning.x + 1, currentBeginning.y));
                add(new Point( currentBeginning.x, currentBeginning.y + 1));
                add(new Point( currentBeginning.x, currentBeginning.y - 1));
            }};

            targets.removeIf(target -> !board.isIndexOk(target.x, target.y));
            targets.removeIf(target -> !board.isTarget(target));

            int length = targets.size();
            Random random = new Random();
            int targetIndex = random.nextInt(length);

            return targets.get(targetIndex);
        }

        private Point Random2Points() {
            ArrayList<Point> targets = new ArrayList<>();

            if (isHorizontal) {
                targets.add(new Point(currentBeginning.x - 1, currentBeginning.y));
                targets.add(new Point(currentEnd.x + 1, currentEnd.y));
            } else {
                targets.add(new Point(currentBeginning.x, currentBeginning.y - 1));
                targets.add(new Point(currentEnd.x, currentEnd.y + 1));
            }

            targets.removeIf(target -> !board.isIndexOk(target.x, target.y));
            targets.removeIf(target -> !board.isTarget(target));

            int length = targets.size();
            Random random = new Random();
            int targetIndex = random.nextInt(length);

            return targets.get(targetIndex);
        }

    }

}
