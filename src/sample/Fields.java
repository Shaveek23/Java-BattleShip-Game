package sample;

import Game.Mode;

public class Fields {
    public Field[][] fields;

    public Fields()
    {
        fields = new Field[Mode.getHeight()][Mode.getWidth()];
    }


}
