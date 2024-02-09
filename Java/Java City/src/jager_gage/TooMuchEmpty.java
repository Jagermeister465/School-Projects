package jager_gage;

/**
 * description: A custom exception to be thrown if rezone finds
 * more than 4 empty tiles in the city.
 * @author Gage Jager
 */
public class TooMuchEmpty extends Exception{
    protected TooMuchEmpty() {
        super("defaultMessage");
    }
}
