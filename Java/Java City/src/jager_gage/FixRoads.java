package jager_gage;

/**
 * description: The extended Visitor class for fixing roads.
 * Overrides the 5 accept functions from Visitor to perform tasks
 * based upon the type of the current tile.
 * @author Gage Jager
 */
public class FixRoads implements Visitor{

    /**
     * description: The Overridden acceptEmpty function.
     * This visitor only cares for roads, so this function is only present
     * to allow the class to compile.
     * @author Gage Jager
     * @param e The current tile - is empty in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this function.
     */
    public void acceptEmpty(Empty e, int tileType, int color, Tile[][] grid) {
        //Not a road, don't need to worry about adjacencies.
    }

    /**
     * description: The Overridden acceptBuilding function.
     * This visitor only cares for roads, so this function is only present
     * to allow the class to compile.
     * @author Gage Jager
     * @param b The current tile - is a building in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this function.
     */
    public void acceptBuilding(Building b, int tileType, int color, Tile[][] grid) {
        //Not a road, don't need to worry about adjacencies.
    }

    /**
     * description: The Overridden acceptWater function.
     * This visitor only cares for roads, so this function is only present
     * to allow the class to compile.
     * @author Gage Jager
     * @param w The current tile - is water in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this function.
     */
    public void acceptWater(Water w, int tileType, int color, Tile[][] grid) {
        //Not a road, don't need to worry about adjacencies.
    }

    /**
     * description: The Overridden acceptGreenspace function.
     * This visitor only cares for roads, so this function is only present
     * to allow the class to compile.
     * @author Gage Jager
     * @param g The current tile - is a greenspace in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this function.
     */
    public void acceptGreenspace(Greenspace g, int tileType, int color, Tile[][] grid) {
        //Not a road, don't need to worry about adjacencies.
    }

    /**
     * description: The Overridden acceptStreet function.
     * If the current tile is a street, make an instance of the isRoad visitor
     * to send to the tiles above, below, and to the left and right of the
     * current tile, and find out if it is a road.  Then, call the setAdjacencies
     * function on the current street tile to update its symbol to a more
     * accurate character.
     * @author Gage Jager
     * @param s The current tile - is a street in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid The city layout, used to pass other tiles to isRoad visitor.
     */
    public void acceptStreet(Street s, int tileType, int color, Tile[][] grid) {
        //Is a road, need to check adjacencies.
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        //GRADING: NESTED.
        IsRoad checkAdjacency = new IsRoad();
        //Check up.
        if (s.yCoord > 0) {
            grid[s.xCoord][s.yCoord - 1].accept(checkAdjacency, tileType, color, grid);
            up = checkAdjacency.getLastVisitWasRoad();
        }
        //Check down.
        if (s.yCoord < 6) {
            grid[s.xCoord][s.yCoord + 1].accept(checkAdjacency, tileType, color, grid);
            down = checkAdjacency.getLastVisitWasRoad();
        }
        //Check left.
        if (s.xCoord > 0) {
            grid[s.xCoord - 1][s.yCoord].accept(checkAdjacency, tileType, color, grid);
            left = checkAdjacency.getLastVisitWasRoad();
        }
        //Check right.
        if (s.xCoord < 6) {
            grid[s.xCoord + 1][s.yCoord].accept(checkAdjacency, tileType, color, grid);
            right = checkAdjacency.getLastVisitWasRoad();
        }

        //Call the street's setAdjacencies function with the results of our check.
        s.setAdjacencies(left, up, down, right);
    }
}
