package sample.Models;

import Game.Mode;
import sample.Models.Field;

public class Fields {
    public Field[][] fields;

    public Fields()
    {
        fields = new Field[Mode.getHeight()][Mode.getWidth()];
    }


}
