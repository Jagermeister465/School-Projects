package jager_gage;

/**
 * description: The extended Visitor class for finding if a tile is a road.
 * Overrides the 5 accept functions from Visitor to perform tasks
 * based upon the type of the current tile.
 * @author Gage Jager
 */
public class IsRoad implements Visitor{
    private boolean lastVisitWasRoad;

    //IsRoad constructor, simply defaults lastVisitWasRoad to false.
    protected IsRoad() {
        lastVisitWasRoad = false;
    }

    /**
     * description: The Overridden acceptEmpty function.
     * Since the visitor wants to find if a tile is a road,
     * an empty tile means to set lastVisitWasRoad to false.
     * @author Gage Jager
     * @param e The current tile - is empty in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptEmpty(Empty e, int tileType, int color, Tile[][] grid) {
        //Not a road.
        lastVisitWasRoad = false;
    }

    /**
     * description: The Overridden acceptBuilding function.
     * Since the visitor wants to find if a tile is a road,
     * a building tile means to set lastVisitWasRoad to false.
     * @author Gage Jager
     * @param b The current tile - is a building in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptBuilding(Building b, int tileType, int color, Tile[][] grid) {
        //Not a road.
        lastVisitWasRoad = false;
    }

    /**
     * description: The Overridden acceptWater function.
     * Since the visitor wants to find if a tile is a road,
     * a water tile means to set lastVisitWasRoad to false.
     * @author Gage Jager
     * @param w The current tile - is water in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptWater(Water w, int tileType, int color, Tile[][] grid) {
        //Not a road.
        lastVisitWasRoad = false;
    }

    /**
     * description: The Overridden acceptGreenspace function.
     * Since the visitor wants to find if a tile is a road,
     * a greenspace tile means to set lastVisitWasRoad to false.
     * @author Gage Jager
     * @param g The current tile - is a greenspace in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptGreenspace(Greenspace g, int tileType, int color, Tile[][] grid) {
        //Not a road.
        lastVisitWasRoad = false;
    }

    /**
     * description: The Overridden acceptStreet function.
     * Since the visitor wants to find if a tile is a road,
     * a street tile means to set lastVisitWasRoad to true.
     * @author Gage Jager
     * @param s The current tile - is a street in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptStreet(Street s, int tileType, int color, Tile[][] grid) {
        //Is a road.
        lastVisitWasRoad = true;
    }

    /**
     * description: A simple getter to allow access to lastVisitWasRoad,
     * used to let a FixRoads visitor know if a tile was a road.
     * @author Gage Jager
     */
    protected boolean getLastVisitWasRoad() {
        return lastVisitWasRoad;
    }
}
