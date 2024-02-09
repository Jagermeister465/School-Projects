package jager_gage;

/**
 * description: The Visitor interface is used to define the accept
 * functions that the implemented visitors must have to compile.
 * There is an accept for each of the 5 derived Tiles.
 * @author Gage Jager
 */
public interface Visitor {
    void acceptEmpty(Empty e, int tileType, int color, Tile[][] grid);
    void acceptBuilding(Building b, int tileType, int color, Tile[][] grid);
    void acceptWater(Water w, int tileType, int color, Tile[][] grid);
    void acceptGreenspace(Greenspace g, int tileType, int color, Tile[][] grid);
    void acceptStreet(Street s, int tileType, int color, Tile[][] grid);
}
