package sample.Models;

import Game.Mode;
import sample.Models.Field;

public class Fields {
    public Field[][] fields;

    public Fields(int height, int width)
    {
        fields = new Field[height][width];
    }


}
