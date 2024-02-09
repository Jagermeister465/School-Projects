package jager_gage;

/**
 * description: A custom exception to be used when the user inputs
 * an integer outside the range of the current menu options.
 * @author Gage Jager
 */
public class MainMenuOutOfBounds extends Exception{
    protected MainMenuOutOfBounds() {
        super("defaultMessage");
    }
}
