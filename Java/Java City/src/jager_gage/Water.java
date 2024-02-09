package jager_gage;

/**
 * description: The extended Tile class for Water tiles.
 * Extends the Tile constructor to additionally set the
 * correct symbol, and Overrides the Tile accept function
 * to be specific to the tile type.
 * @author Gage Jager
 */
public class Water extends Tile{

    //Water Constructor
    protected Water(int xInput, int yInput) {
        super(xInput, yInput);
        symbol = '~';
    }

    /**
     * description: The water class accept function.
     * Takes the passed parameters from the accept in City
     * and lets the visitor know what tile type it is.
     * @author Gage Jager
     * @param v An unspecified type of visitor.
     * @param tileType An int representing tile choice.
     *                 Only used by ChangeColor visitors.
     * @param color An int representing tile color.
     *              Only used by ChangeColor visitors.
     * @param grid The city tile grid.
     *             Only used by Rezone and FixRoads visitors.
     */
    protected void accept(Visitor v, int tileType, int color, Tile[][] grid) {
        v.acceptWater(this, tileType, color, grid);
    }
}
