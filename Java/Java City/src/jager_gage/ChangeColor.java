package jager_gage;

/**
 * description: The extended Visitor class for changing colors.
 * Overrides the 5 accept functions from Visitor to perform tasks
 * based upon the type of the current tile.
 * @author Gage Jager
 */
public class ChangeColor implements Visitor{

    /**
     * description: The Overridden acceptEmpty function.
     * Empty tiles are never recolored, this function only exists
     * so the class compiles.
     * @author Gage Jager
     * @param e The current tile - is empty in this function.
     * @param tileType An int representing the type of tile to change colors of.
     * @param color An int representing the color to change to.
     * @param grid Not used by this Visitor type.
     */
    public void acceptEmpty(Empty e, int tileType, int color, Tile[][] grid) {
        //Empty tiles do not get recolored, do nothing.
    }

    /**
     * description: The Overridden acceptBuilding function.
     * Buildings are recolored if the user selected tileType 1.
     * If the tileType is 1, change the color based on the user inputted color.
     * Else, do nothing.
     * @author Gage Jager
     * @param b The current tile - is a street in this function.
     * @param tileType An int representing the type of tile to change colors of.
     * @param color An int representing the color to change to.
     * @param grid Not used by this Visitor type.
     */
    public void acceptBuilding(Building b, int tileType, int color, Tile[][] grid) {
        //If tileType is 1, we are recoloring buildings.
        if (tileType == 1) {
            //Determine what color they inputted.
            if (color == 1) {
                b.color = ColorText.Color.RED;
            }
            else if (color == 2) {
                b.color = ColorText.Color.ORANGE;
            }
            else if (color == 3) {
                b.color = ColorText.Color.BLUE;
            }
            else if (color == 4) {
                b.color = ColorText.Color.GREEN;
            }
            else {
                b.color = ColorText.Color.BLACK;
            }
        }
    }

    /**
     * description: The Overridden acceptWater function.
     * Water is recolored if the user selected tileType 3.
     * If the tileType is 3, change the color based on the user inputted color.
     * Else, do nothing.
     * @author Gage Jager
     * @param w The current tile - is water in this function.
     * @param tileType An int representing the type of tile to change colors of.
     * @param color An int representing the color to change to.
     * @param grid Not used by this Visitor type.
     */
    public void acceptWater(Water w, int tileType, int color, Tile[][] grid) {
        //If tileType is 3, we are recoloring water/greenspace.
        if (tileType == 3) {
            //Determine what color they inputted.
            if (color == 1) {
                w.color = ColorText.Color.RED;
            }
            else if (color == 2) {
                w.color = ColorText.Color.ORANGE;
            }
            else if (color == 3) {
                w.color = ColorText.Color.BLUE;
            }
            else if (color == 4) {
                w.color = ColorText.Color.GREEN;
            }
            else {
                w.color = ColorText.Color.BLACK;
            }
        }
    }

    /**
     * description: The Overridden acceptGreenspace function.
     * Greenspaces are recolored if the user selected tileType 3.
     * If the tileType is 3, change the color based on the user inputted color.
     * Else, do nothing.
     * @author Gage Jager
     * @param g The current tile - is a greenspace in this function.
     * @param tileType An int representing the type of tile to change colors of.
     * @param color An int representing the color to change to.
     * @param grid Not used by this Visitor type.
     */
    public void acceptGreenspace(Greenspace g, int tileType, int color, Tile[][] grid) {
        //If tileType is 3, we are recoloring water/greenspace.
        if (tileType == 3) {
            //Determine what color they inputted.
            if (color == 1) {
                g.color = ColorText.Color.RED;
            }
            else if (color == 2) {
                g.color = ColorText.Color.ORANGE;
            }
            else if (color == 3) {
                g.color = ColorText.Color.BLUE;
            }
            else if (color == 4) {
                g.color = ColorText.Color.GREEN;
            }
            else {
                g.color = ColorText.Color.BLACK;
            }
        }
    }

    /**
     * description: The Overridden acceptStreet function.
     * Streets are recolored if the user selected tileType 2.
     * If the tileType is 2, change the color based on the user inputted color.
     * Else, do nothing.
     * @author Gage Jager
     * @param s The current tile - is a street in this function.
     * @param tileType An int representing the type of tile to change colors of.
     * @param color An int representing the color to change to.
     * @param grid Not used by this Visitor type.
     */
    public void acceptStreet(Street s, int tileType, int color, Tile[][] grid) {
        //If tileType is 2, we are recoloring streets.
        if (tileType == 2) {
            //Determine what color they inputted.
            if (color == 1) {
                s.color = ColorText.Color.RED;
            }
            else if (color == 2) {
                s.color = ColorText.Color.ORANGE;
            }
            else if (color == 3) {
                s.color = ColorText.Color.BLUE;
            }
            else if (color == 4) {
                s.color = ColorText.Color.GREEN;
            }
            else {
                s.color = ColorText.Color.BLACK;
            }
        }
    }
}
