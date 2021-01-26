package Game;

import Game.Ships.ShipsParameters;

import java.util.ArrayList;

public class Mode {
    private int width;
    private int height;
    private ShipsParameters[] shipsParameters;
    private int currentIndex = 0;
    private int[] shipsSizes;

    public Mode(int width, int height, ShipsParameters[] shipsParameters) {
        this.width = width;
        this.height = height;
        this.shipsParameters = shipsParameters;
        ArrayList<Integer> shipsSizes = new ArrayList<Integer>();

        for (ShipsParameters p : shipsParameters) {
            for (int i = 0; i < p.getCount(); i++) {
                shipsSizes.add(p.getSize());
            }
        }
        this.shipsSizes = shipsSizes.stream().mapToInt(i->i).toArray();

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ShipsParameters[] getShipsParameters() {
        return this.shipsParameters;
    }

    public int nextShipSize() {
        if (currentIndex < shipsSizes.length) {
            int size = shipsSizes[currentIndex];
            currentIndex = currentIndex + 1;
            return size;
        }

        return 0;
    }

    public void bringBackSize() {
        currentIndex--;
    }

    public int getCurrentShipNo() {
        return currentIndex - 1;
    }

   public String getShips() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ships: ");
        for (var param : shipsParameters) {
            sb.append(" ");
            sb.append(param.getCount());
            sb.append(" x ");
            sb.append(param.getSize());
            sb.append("-size ships |");
        }
        return sb.toString();
   }

}
