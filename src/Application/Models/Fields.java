package Application.Models;

public class Fields {
    private Field[][] fields;

    public Field get(int x, int y) {
        return fields[y][x];
    }

    public void set(int x, int y, Field value) {
        fields[y][x] = value;
    }

    public Fields(int height, int width)
    {
        fields = new Field[height][width];
    }


}
