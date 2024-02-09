package jager_gage;


/**
 * Grading tags in for all lines marked with *		___ Yes
 *
 * The visitor pattern is used 				        ___ Yes
 * Handles bad input with 1 try-catch			    ___ Yes (Main Menu wrapped by try, catch afterwards before loop)
 * Threw the exception in tier 8 (rezone)			___ Yes (TooMuchEmpty exception)
 *
 * Tier 1: running and menu working 			    ___ Yes
 * Tier 2: set any object at 0, 0 				    ___ Yes
 * Tier 3: set any object a anywhere			    ___ Yes
 * Tier 4: handles bad input at this point			___ Yes
 * Tier 5: default grid displays properly 			___ Yes
 * Tier 6: count types * 					        ___ Yes
 * Tier 7: coloring and menus completed*			___ Yes
 * Tier 8: Rezone *					                ___ Yes
 * Tier 9: Fix roads*			  		            ___ Yes
 *      All adjacent pullable objects removed		      ___ I'm gonna be real I don't know what a "pullable" object is
 *      At least one pullable objects are pulled inwards  ___ See above
 */


import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * description: The start of the Java City program.
 * Displays the city and menu options, and handles program input.
 * @author Dr. Lisa Rebenitsch and Gage Jager
 */
public class CityStart {
    public static Scanner cin;
    public static void main(String[] args) {

        City c = new City();
        cin = new Scanner(System.in);
        String menu =
                "1) Set Tile\n"+
                "2) Make Default City\n"+
                "3) Count Zones\n"+
                "4) Set Tile Color\n"+
                "5) Rezone\n"+
                "6) Fix Roads\n"+
                "0) Quit\n";
        int input = -1;
        while(input != 0) {
            try {
                System.out.println(c);
                System.out.println(menu);
                System.out.print("Choice:> ");

                //Will throw InputMismatchException if it reads in
                //a non-integer (or at least that's what the internet says
                //it should do)
                input = cin.nextInt();

                //Check if input is within Main Menu bounds (0-6)
                //If not, throw MainMenuOutOfBounds exception.
                if (input < 0 || input > 6) {
                    throw new MainMenuOutOfBounds();
                }

                //Determine which menu option was chosen.
                if (input == 1) {
                    //Set Tile input
                    System.out.print("Input tile type 1) greenspace 2) water "
                                    + "3) road 4) building 5) empty:> ");
                    //All three inputs need to be checked for:
                    //Being an integer (done through nextInt)
                    //Being in range (MainMenuOutOfBounds)
                    int tileChoice = cin.nextInt();
                    if (tileChoice < 1 || tileChoice > 5) {
                        throw new MainMenuOutOfBounds();
                    }

                    System.out.print("Input location (x y): ");
                    int xChoice = cin.nextInt();
                    if (xChoice < 0 || xChoice > 6) {
                        throw new MainMenuOutOfBounds();
                    }
                    int yChoice = cin.nextInt();
                    if (yChoice < 0 || yChoice > 6) {
                        throw new MainMenuOutOfBounds();
                    }

                    //Call Set Tile function
                    c.setTile(tileChoice, xChoice, yChoice);
                }
                else if (input == 2) {
                    //Make Default City function
                    c.defaultCity();
                }
                else if (input == 3) {
                    //Count Zones function
                    GetCounts counter = new GetCounts();
                    //GRADING: COUNT.
                    c.accept(counter, 0, 0);
                    System.out.println(counter);
                }
                else if (input == 4) {
                    //Set Tile Color input
                    System.out.print("Input tile type 1) building 2) road 3) non-structure:> ");

                    //Both inputs need to be checked for being
                    //an int and being in range.
                    int tileType = cin.nextInt();
                    if (tileType < 1 || tileType > 3) {
                        throw new MainMenuOutOfBounds();
                    }

                    System.out.print("Input color 1) red 2) orange 3) blue"
                                    + " 4) green 5) black:> ");
                    int colorChoice = cin.nextInt();
                    if (colorChoice < 1 || colorChoice > 5) {
                        throw new MainMenuOutOfBounds();
                    }

                    //Set Tile Color function
                    ChangeColor colorer = new ChangeColor();
                    //GRADING: COLOR.
                    c.accept(colorer, tileType, colorChoice);
                }
                else if (input == 5) {
                    //Check if we can Rezone
                    GetCounts g = new GetCounts();
                    c.accept(g, 0, 0);
                    int numEmpty = g.getNumEmpty();
                    if (numEmpty > 4) {
                        throw new TooMuchEmpty();
                    }

                    //Rezone Function
                    Rezone rezoner = new Rezone();
                    //GRADING: REZONE.
                    c.accept(rezoner, 0, 0);

                }
                else if (input == 6) {
                    //Fix Roads function
                    FixRoads fixer = new FixRoads();
                    c.accept(fixer, 0, 0);
                }
            }
            catch(InputMismatchException e) {
                System.out.println("Please input an integer");
                //clear bad input
                String clearInput = cin.nextLine();
            }
            catch(MainMenuOutOfBounds e) {
                System.out.println("Number is out of range");
            }
            catch(TooMuchEmpty e) {
                System.out.println("Insufficient open areas");
            }
        }


    }

}
