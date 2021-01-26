package Game;

import Game.Field.EmptyField;
import Game.Field.IField;
import Game.Field.ShipPart;
import Game.Ships.Ship;
import javafx.util.Pair;
import sample.Models.Field;
import sample.Models.Fields;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private int width;
    private int height;

    private IField[][] fields;
    private HashMap<Pair<Integer, Integer>, IField> notHitYet = new HashMap<Pair<Integer, Integer>, IField>();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        fields = new IField[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fields[i][j] = new EmptyField(j, i);
            }
        }
    }
    
    public boolean isPossibleToPlaceShip(int size, boolean horizontal, int xStart, int yStart) {
        int k = horizontal ? xStart : yStart;
        int limit = horizontal ? width : height;
        int dx = horizontal ? 1 : 0;
        int dy = horizontal ? 0 : 1;
        
        for (int i = 0; i < size; i++) {
            if (i + k >= limit)
                return false;
            
            if (fields[yStart + i * dy][xStart + i * dx].isShip())
                return false;

        }

        if (dx > 0) {
            if (hasNeighboursHorizontal(size, xStart, yStart)) {
                return false;
            }
        } else {
            if (hasNeighboursVertical(size, xStart, yStart)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasNeighboursHorizontal(int size, int xStart, int yStart) {
        //sprawdzenie początku
        if (isIndexOk(xStart - 1, yStart)) {
            if (fields[yStart][xStart - 1].isShip())
                return true;
        }

        //sprawdzenie środka
        for (int i = 0; i < size; i++) {
            if (isIndexOk(xStart + i, yStart + 1)) {
                if (fields[yStart + 1][xStart + i].isShip())
                    return true;
            }

            if (isIndexOk(xStart + i, yStart - 1)) {
                if (fields[yStart - 1][xStart + i].isShip()) {
                    return true;
                }
            }
        }

        //sprawdzenie końca

        if (isIndexOk(xStart + size, yStart)) {
            if (fields[yStart][xStart + size].isShip()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasNeighboursVertical(int size, int xStart, int yStart) {
        //sprawdzenie początku
        if (isIndexOk(xStart, yStart - 1)) {
            if (fields[yStart - 1][xStart].isShip())
                return true;
        }

        //sprawdzenie środka
        for (int i = 0; i < size; i++) {
            if (isIndexOk(xStart + 1, yStart + i)) {
                if (fields[yStart + i][xStart + 1].isShip())
                    return true;
            }

            if (isIndexOk(xStart - 1, yStart + i)) {
                if (fields[yStart + i][xStart - 1].isShip()) {
                    return true;
                }
            }
        }

        //sprawdzenie końca

        if (isIndexOk(xStart, yStart + size)) {
            if (fields[yStart + size][xStart].isShip()) {
                return true;
            }
        }

        return false;
    }

    private void markWaterAroundShip(Ship ship) {
        if (ship.isHorizontal()) {
            markWaterHorizontal(ship);
        } else {
            markWaterVertical(ship);
        }
    }

    private void markWaterHorizontal(Ship ship) {
        Point startPoint = ship.getBeginningCoordinates();
        int xStart = startPoint.x;
        int yStart = startPoint.y;
        int size = ship.getSize();

        //odkrycie początku
        if (isIndexOk(xStart - 1, yStart)) {
            shoot(fields[yStart][xStart - 1].getPoint());
        }

        //odkrycie środka
        for (int i = 0; i < size; i++) {
            if (isIndexOk(xStart + i, yStart + 1)) {
                shoot(fields[yStart + 1][xStart + i].getPoint());
            }

            if (isIndexOk(xStart + i, yStart - 1)) {
                shoot(fields[yStart - 1][xStart + i].getPoint());
            }
        }

        //sprawdzenie końca

        if (isIndexOk(xStart + size, yStart)) {
            shoot(fields[yStart][xStart + size].getPoint());
        }
    }

    private void markWaterVertical(Ship ship) {
        Point startPoint = ship.getBeginningCoordinates();
        int xStart = startPoint.x;
        int yStart = startPoint.y;
        int size = ship.getSize();

        //sprawdzenie początku
        if (isIndexOk(xStart, yStart - 1)) {
            shoot(fields[yStart - 1][xStart].getPoint());
        }

        //sprawdzenie środka
        for (int i = 0; i < size; i++) {
            if (isIndexOk(xStart + 1, yStart + i)) {
                shoot(fields[yStart + i][xStart + 1].getPoint());
            }

            if (isIndexOk(xStart - 1, yStart + i)) {
                shoot(fields[yStart + i][xStart - 1].getPoint());
            }
        }

        //sprawdzenie końca
        if (isIndexOk(xStart, yStart + size)) {
            shoot(fields[yStart + size][xStart].getPoint());
        }
    }

    public boolean isIndexOk(int height, int width) {
        return (height >= 0 && height < Mode.getHeight()) && (width >= 0 && width < Mode.getWidth());
    }
    
    public void placeShip(Ship ship) {
        for (ShipPart part: ship) {
            fields[part.getY()][part.getX()] = part;
        }
    }

    public void zipFields() {
        notHitYet = new HashMap<Pair<Integer, Integer>, IField>();
        for (var column: fields) {
            for (var field: column) {
                notHitYet.put(new Pair(field.getX(), field.getY()), field);
            }
        }
    }

    public Point randomizeTarget() {
        Random random = new Random();
        int hitIndex = random.nextInt(notHitYet.size());
        IField target = (IField)notHitYet.values().toArray()[hitIndex];

        if (checkIfDeadField(target)) {
            notHitYet.remove(new Pair<Integer, Integer>(target.getX(), target.getY()));
            return randomizeTarget();
        }


        return new Point(target.getX(), target.getY());
    }

    private boolean checkIfDeadField(IField target) {
        int x = target.getX();
        int y = target.getY();
        boolean f1 = true;
        boolean f2 = true;
        boolean f3 = true;
        boolean f4 = true;

        if (isIndexOk(y - 1, x))
            if (!fields[y - 1][x].isHit())
                f1 = false;


        if (isIndexOk(y + 1, x))
            if (!fields[y + 1][x].isHit())
                f2 = false;

        if (isIndexOk(y, x + 1))
            if (!fields[y][x + 1].isHit())
                f3 = false;

        if (isIndexOk(y, x - 1))
            if (!fields[y][x - 1].isHit())
                f4 = false;

        if (f1 && f2 && f3 && f4)
            return true;

        return false;
    }

    public boolean shoot(Point target) {
        fields[target.y][target.x].hit();
        notHitYet.remove(new Pair<Integer, Integer>(target.x, target.y));
        if (fields[target.y][target.x].isShip()) {
            Ship ship = ((ShipPart)fields[target.y][target.x]).getShip();
            if (ship.isDestroyed())
                markWaterAroundShip(ship);
            return true;
        }
        return false;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isTarget(Point point) {
        return !fields[point.y][point.x].isHit();
    }

    public Fields getPreviewFieldsModel()
    {
        Fields fieldsModel = new Fields();

        for (int i = 0; i < Mode.getHeight(); i++) {
            for (int j = 0; j < Mode.getWidth(); j++) {
                if (!this.fields[i][j].isHit()) {
                    if (this.fields[i][j].isShip())
                        fieldsModel.fields[i][j] = Field.PREVIEW;
                    else
                        fieldsModel.fields[i][j] = Field.UNKNOWN;
                } else if (this.fields[i][j].isShip()) {
                    fieldsModel.fields[i][j] = Field.DESTROYED;
                } else {
                    fieldsModel.fields[i][j] = Field.MISS;
                }
            }
        }
        return fieldsModel;
    }

    public Fields getFieldsModel() {
        Fields fieldsModel = new Fields();

        for (int i = 0; i < Mode.getHeight(); i++) {
            for (int j = 0; j < Mode.getWidth(); j++) {
                if (!this.fields[i][j].isHit()) {
                    fieldsModel.fields[i][j] = Field.UNKNOWN;
                } else if (this.fields[i][j].isShip()) {
                    fieldsModel.fields[i][j] = Field.DESTROYED;
                } else {
                    fieldsModel.fields[i][j] = Field.MISS;
                }
            }
        }
        return fieldsModel;
    }
}
