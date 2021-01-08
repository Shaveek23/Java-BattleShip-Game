package Game;

import Game.Ships.ShipsParameters;

import java.util.ArrayList;

public class Mode {
    private static int width;
    private static int height;
    private static ShipsParameters[] shipsParameters;
    private static int currentIndex = 0;
    private static int[] shipsSizes;

    public Mode(int width, int height, ShipsParameters[] shipsParameters) {
        Mode.width = width;
        Mode.height = height;
        Mode.shipsParameters = shipsParameters;
        ArrayList<Integer> shipsSizes = new ArrayList<Integer>();

        for (ShipsParameters p : shipsParameters) {
            for (int i = 0; i < p.getCount(); i++) {
                shipsSizes.add(p.getSize());
            }
        }
        Mode.shipsSizes = shipsSizes.stream().mapToInt(i->i).toArray();

    }

    public static int getWidth() {
        return Mode.width;
    }

    public static int getHeight() {
        return Mode.height;
    }

    public static ShipsParameters[] getShipsParameters() {
        return Mode.shipsParameters;
    }

    public static int nextShipSize() {
        if (currentIndex < shipsSizes.length) {
            int size = shipsSizes[currentIndex];
            currentIndex = currentIndex + 1;
            return size;
        }

        return 0;
    }

    public static void bringBackSize() {
        currentIndex--;
    }

    public static int getCurrentShipNo() {
        return currentIndex - 1;
    }

   public static String getShips() {
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
