package exceptions;

public class ConfigAlreadyExistingException extends Exception {
	public void printError() {
		System.out.println(
				"Error: configuration already exists. Please choose different parameters or select an existing configuration.");
	}
}
