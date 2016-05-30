package exceptions;

/**
 * This exception is thrown when a specified board configuration already exists
 * inside the config file
 * 
 * @author Riccardo
 *
 */
public class ConfigAlreadyExistingException extends Exception {
	public String printError() {
		return "Error: configuration already exists. Please choose different parameters or select an existing configuration.";
	}
}
