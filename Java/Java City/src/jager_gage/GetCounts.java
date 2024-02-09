package jager_gage;

/**
 * description: The extended Visitor class for counting tiles.
 * Overrides the 5 accept functions from Visitor to perform tasks
 * based upon the type of the current tile.
 * @author Gage Jager
 */
public class GetCounts implements Visitor{

    private int numEmpty;
    private int numBuilding;
    private int numWater;
    private int numGreenspace;
    private int numStreet;

    //GetCounts constructor, just sets all counts to 0.
    public GetCounts() {
        numEmpty = 0;
        numBuilding = 0;
        numWater = 0;
        numGreenspace = 0;
        numStreet = 0;
    }

    /**
     * description: The Overridden acceptEmpty function.
     * This function will increment the number of tiles of each type,
     * in this case, the number of empty tiles.
     * @author Gage Jager
     * @param e The current tile - is empty in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptEmpty(Empty e, int tileType, int color, Tile[][] grid) {
        numEmpty += 1;
    }

    /**
     * description: The Overridden acceptBuilding function.
     * This function will increment the number of tiles of each type,
     * in this case, the number of building tiles.
     * @author Gage Jager
     * @param b The current tile - is a building in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptBuilding(Building b, int tileType, int color, Tile[][] grid) {
        numBuilding += 1;
    }

    /**
     * description: The Overridden acceptWater function.
     * This function will increment the number of tiles of each type,
     * in this case, the number of water tiles.
     * @author Gage Jager
     * @param w The current tile - is water in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptWater(Water w, int tileType, int color, Tile[][] grid) {
        numWater += 1;
    }

    /**
     * description: The Overridden acceptGreenspace function.
     * This function will increment the number of tiles of each type,
     * in this case, the number of greenspace tiles.
     * @author Gage Jager
     * @param g The current tile - is a greenspace in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptGreenspace(Greenspace g, int tileType, int color, Tile[][] grid) {
        numGreenspace += 1;
    }

    /**
     * description: The Overridden acceptStreet function.
     * This function will increment the number of tiles of each type,
     * in this case, the number of street tiles.
     * @author Gage Jager
     * @param s The current tile - is a street in this function.
     * @param tileType Not used by this Visitor.
     * @param color Not used by this Visitor type.
     * @param grid Not used by this Visitor type.
     */
    public void acceptStreet(Street s, int tileType, int color, Tile[][] grid) {
        numStreet += 1;
    }

    /**
     * description: A simple getter function to allow something access
     * to the number of empty tiles, used to determine if we can Rezone
     * the city.
     * @author Gage Jager
     */
    protected int getNumEmpty() {
        return numEmpty;
    }

    /**
     * description: The Overridden toString function, used to
     * format the output of the Visitor to allow the
     * Menu to simply use System.out.println(GetCounts).
     * @author Gage Jager
     */
    public String toString() {
        return    "Empty:> " + numEmpty + "\n"
                + "Buildings:> " + numBuilding +"\n"
                + "Greenspaces:> " + numGreenspace + "\n"
                + "Roads:> " + numStreet + "\n"
                + "Water:> " + numWater;
    }
}
