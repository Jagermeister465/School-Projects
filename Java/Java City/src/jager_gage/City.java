package jager_gage;

/**
 * description: The class in charge of managing a city.
 * The class has a 7*7 ArrayList of Tiles to represent
 * the city, and can accept a visitor and pass it along
 * to each of the tiles for completing various functions.
 * @author Gage Jager
 */
public class City {

    //Every instance of City needs a grid (admittedly we are only using one
    //city in the program but whatever)
    private Tile[][] grid = new Tile[7][7];

    //City constructor -- fills the grid with all empty tiles.
    protected City() {
        //Set the new City's grid to all Empty Tiles.
        for(int y = 0; y < 7; y++) {
            for(int x = 0; x < 7; x++) {
                grid[x][y] = new Empty(x, y);
            }
        }
    }

    /**
     * description: This function overrides the toString function to allow
     * the main menu to simply print the city with System.out.println(city),
     * rather than have to call any specific formatting function.
     * @author Gage Jager
     * @return String - The string representation of the city layout.
     */
    public String toString() {

        //The IDE recommended I use a string builder instead
        //of concatenating over and over.  It works, so whatever.
        StringBuilder output = new StringBuilder("\n");

        for(int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                //Add every color-formatted symbol to the string.
                output.append(ColorText.colorString(grid[x][y].symbol, grid[x][y].color));
            }
            //After every row of the city, add a newline to the string.
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * description: The function in charge of changing a tile to a different
     * type, at a location specified by the user.  Uses an if else chain
     * to determine which tile type the user chose.
     * @author Gage Jager
     * @param tileChoice An int representing the tile type to create.
     * @param xChoice An int representing the column to put the new tile.
     * @param yChoice An int representing the row to put the new tile.
     */
    protected void setTile(int tileChoice, int xChoice, int yChoice) {
        if(tileChoice == 1) {
            grid[xChoice][yChoice] = new Greenspace(xChoice, yChoice);
        }
        else if(tileChoice == 2) {
            grid[xChoice][yChoice] = new Water(xChoice, yChoice);
        }
        else if(tileChoice == 3) {
            grid[xChoice][yChoice] = new Street(xChoice, yChoice);
        }
        else if(tileChoice == 4) {
            grid[xChoice][yChoice] = new Building(xChoice, yChoice);
        }
        else {
            grid[xChoice][yChoice] = new Empty(xChoice, yChoice);
        }
    }

    /**
     * description: The city's accept function.  Loops across every
     * tile in the grid and calls the tile accept function, passing
     * along the parameters.
     * @author Gage Jager
     * @param v An unspecified type of visitor.
     * @param tileType An int specifying tile type.
     *                 Only used by ChangeColor visitors.
     *                 Otherwise, it is ignored by the visitor.
     * @param color An int specifying color choice.
     *              Only used by ChangeColor visitors.
     *              Otherwise, it is ignored by the visitor.
     */
    protected void accept(Visitor v, int tileType, int color) {
        for(int y = 0; y < 7; y++) {
            for(int x = 0; x < 7; x++) {
                grid[x][y].accept(v, tileType, color, grid);
            }
        }
    }

    /**
     * description: The function for loading the default city layout.
     * Most likely some of the worst code you've ever seen, but hey,
     * it works.
     * @author Gage Jager
     */
    protected void defaultCity() {
        //Get ready for some of the worst
        //code you've ever seen.
        grid[0][0] = new Street(0,0);
        grid[1][0] = new Street(1,0);
        grid[2][0] = new Street(2,0);
        grid[3][0] = new Street(3,0);
        grid[4][0] = new Street(4,0);
        grid[5][0] = new Street(5,0);
        grid[6][0] = new Street(6,0);
        grid[0][1] = new Street(0,1);
        grid[1][1] = new Building(1,1);
        grid[2][1] = new Street(2,1);
        grid[3][1] = new Empty(3,1);
        grid[4][1] = new Street(4,1);
        grid[5][1] = new Empty(5,1);
        grid[6][1] = new Empty(6,1);
        grid[0][2] = new Street(0,2);
        grid[1][2] = new Street(1,2);
        grid[2][2] = new Street(2,2);
        grid[3][2] = new Street(3,2);
        grid[4][2] = new Street(4,2);
        grid[5][2] = new Water(5,2);
        grid[6][2] = new Water(6,2);
        grid[0][3] = new Street(0,3);
        grid[1][3] = new Building(1,3);
        grid[2][3] = new Street(2,3);
        grid[3][3] = new Empty(3,3);
        grid[4][3] = new Street(4,3);
        grid[5][3] = new Greenspace(5,3);
        grid[6][3] = new Water(6,3);
        grid[0][4] = new Street(0,4);
        grid[1][4] = new Building(1,4);
        grid[2][4] = new Empty(2,4);
        grid[3][4] = new Building(3,4);
        grid[4][4] = new Street(4,4);
        grid[5][4] = new Greenspace(5,4);
        grid[6][4] = new Water(6,4);
        grid[0][5] = new Street(0,5);
        grid[1][5] = new Building(1,5);
        grid[2][5] = new Building(2,5);
        grid[3][5] = new Building(3,5);
        grid[4][5] = new Street(4,5);
        grid[5][5] = new Greenspace(5,5);
        grid[6][5] = new Water(6,5);
        grid[0][6] = new Street(0,6);
        grid[1][6] = new Street(1,6);
        grid[2][6] = new Street(2,6);
        grid[3][6] = new Street(3,6);
        grid[4][6] = new Street(4,6);
        grid[5][6] = new Greenspace(5,6);
        grid[6][6] = new Water(6,6);
    }
}
