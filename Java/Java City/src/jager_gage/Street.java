package jager_gage;

/**
 * description: The extended Tile class for Street tiles.
 * Extends the Tile constructor to additionally set the
 * correct symbol, and Overrides the Tile accept function
 * to be specific to the tile type.  Additionally has a
 * function to determine adjacent roads and change an instance's
 * symbol to a more accurate road.
 * @author Gage Jager
 */
public class Street extends Tile{

    private int adjCode;
    //Street Constructor
    protected Street(int xInput, int yInput) {
        super(xInput, yInput);
        symbol = '━';
        adjCode = 0;
    }

    /**
     * description: The street class accept function.
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
        v.acceptStreet(this, tileType, color, grid);
    }

    /**
     * Indicate the road tiles adjacent to this one.
     * <p>
     * The road tile image chosen is dependent on the tiles around
     * it. This is where the adjacency of road tiles is indicated.
     *
     * @author Dr. Lisa Rebenitsch and Gage Jager
     * @param left True if road tile to left
     * @param top True if road tile to top
     * @param bot True if road tile to lower left
     * @param right True if road tile to lower right
     */
    public void setAdjacencies(boolean left, boolean top, boolean bot, boolean right)
    {
        // Create the adjacency code
        int code = (left ? 1 : 0) | (top ? 2 : 0) | (bot ? 4 : 0) | (right ? 8 : 0);
        if (adjCode == code)
        {
            // We are already set. Do nothing
            return;
        }

        //unicode starts at 2510
        char[] symbols = {
                '━',      // 0 right
                '━',      // 1 right
                '┃',      // 2 ud
                '┛',      // 3 lu
                '┃',      // 4 ud
                '┓',      // 5 ld
                '┃',      // 6 ud
                '┫',     // 7 lud
                '━',      // 8 right
                '━',      // 9 right
                '┗',      // 10 ur
                '┻',     // 11 lur
                '┏',      // 12 dr
                '┳',    // 13 ldr
                '┣',     // 14 udr
                '╋'    // 15 ludr
        };

        // Select the appropriate image
        adjCode = code;

        // Set the symbol to symbols[code]
        symbol = symbols[adjCode];

    }
}
