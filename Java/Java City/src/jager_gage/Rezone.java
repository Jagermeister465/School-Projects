package jager_gage;

/**
 * description: The extended Visitor class for rezoning the city.
 * Overrides the 5 accept functions from Visitor to perform tasks
 * based upon the type of the current tile.
 * @author Gage Jager
 */
public class Rezone implements Visitor{

    /**
     * description: The Overridden acceptEmpty function.
     * This visitor changes an empty tile to a greenspace.
     * Since we have an empty tile, we can make a new greenspace
     * tile and put it at the current location of the empty tile.
     * @author Gage Jager
     * @param e The current tile - is empty in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid The city layout, used to change tiles to greenspaces.
     */
    public void acceptEmpty(Empty e, int tileType, int color, Tile[][] grid) {
        //Change each empty tile to a new Greenspace tile.
        grid[e.xCoord][e.yCoord] = new Greenspace(e.xCoord, e.yCoord);
    }

    /**
     * description: The Overridden acceptBuilding function.
     * This visitor changes an empty tile to a greenspace.
     * Since buildings are not empty tiles, this function is only
     * present to allow the class to compile.
     * @author Gage Jager
     * @param b The current tile - is a building in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid The city layout, used to change tiles to greenspaces.
     */
    public void acceptBuilding(Building b, int tileType, int color, Tile[][] grid) {
        //Rezoning only affects Empty tiles.
    }

    /**
     * description: The Overridden acceptWater function.
     * This visitor changes an empty tile to a greenspace.
     * Since water is not an empty tile, this function is only
     * present to allow the class to compile.
     * @author Gage Jager
     * @param w The current tile - is water in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid The city layout, used to change tiles to greenspaces.
     */
    public void acceptWater(Water w, int tileType, int color, Tile[][] grid) {
        //Rezoning only affects Empty tiles.
    }

    /**
     * description: The Overridden acceptGreenspace function.
     * This visitor changes an empty tile to a greenspace.
     * Since greenspaces are not empty tiles, this function is only
     * present to allow the class to compile.
     * @author Gage Jager
     * @param g The current tile - is a greenspace in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid The city layout, used to change tiles to greenspaces.
     */
    public void acceptGreenspace(Greenspace g, int tileType, int color, Tile[][] grid) {
        //Rezoning only affects Empty tiles.
    }

    /**
     * description: The Overridden acceptStreet function.
     * This visitor changes an empty tile to a greenspace.
     * Since streets are not empty tiles, this function is only
     * present to allow the class to compile.
     * @author Gage Jager
     * @param s The current tile - is a street in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid The city layout, used to change tiles to greenspaces.
     */
    public void acceptStreet(Street s, int tileType, int color, Tile[][] grid) {
        //Rezoning only affects Empty tiles.
    }
}
