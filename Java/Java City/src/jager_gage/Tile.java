package jager_gage;

/**
 * description: The Tile class is an abstract city tile, which defines the
 * variables every Tile will use, regardless of type.  Additionally, the
 * Tile's accept function is defined here, with each type of Tile calling
 * their particular accept in the visitor.
 * @author Gage Jager
 */
abstract public class Tile {

    //Every Tile will hold its symbol, its location in the city,
    //and the color the tile should be output as.
    char symbol;
    int xCoord;
    int yCoord;
    ColorText.Color color;

    //Default Tile Constructor
    //Each Tile has a symbol, coordinates, and a color.
    protected Tile(int xIndex, int yIndex) {
        color = ColorText.Color.BLACK;
        xCoord = xIndex;
        yCoord = yIndex;
    }

    /**
     * description: An abstract accept function that will be overridden
     * by the extended Tile classes.
     * @author Gage Jager
     * @param v An unspecified type of visitor.
     * @param tileType An int representing tile choice.
     *                 Only used by ChangeColor visitors.
     * @param color An int representing tile color.
     *              Only used by ChangeColor visitors.
     * @param grid The city tile grid.
     *             Only used by Rezone and FixRoads visitors.
     */
    protected void accept(Visitor v, int tileType, int color, Tile[][] grid){

    }
}
