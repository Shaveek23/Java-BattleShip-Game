package Player.ComputerPlayer;

import Game.*;
import Game.Ships.Ship;
import Game.Ships.ShipsParameters;
import Player.Player;
import sample.Models.Fields;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player {

    private LastShotTarget lastAccurate = null;
    public ComputerPlayer(Mode mode) {
        super(new Board(mode.getWidth(), mode.getHeight()), mode, new Ship[0]);
        int shipsCount = 0;
        for (ShipsParameters params: mode.getShipsParameters()) {
            shipsCount += params.getCount();
        }
        ships = new Ship[shipsCount];
    }



    @Override
    public void placeShip(Ship ship, int counter) {
        board.placeShip(ship);
        ships[counter] = ship;
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

    @Override
    public Fields getFieldsModel() {
        return this.board.getPreviewFieldsModel();
    }

    public boolean isPossibleToPlaceShip(int nextShipSize, boolean isHorizontal, int x, int y) {
        return this.board.isPossibleToPlaceShip(nextShipSize, isHorizontal, x, y);
    }

    public void zipFields() {
        this.board.zipFields();
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
