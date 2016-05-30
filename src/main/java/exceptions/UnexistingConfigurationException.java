package exceptions;

public class UnexistingConfigurationException extends Exception {
	public String printError() {
		return "Error: the specified configuration doesn't exists, please type the correct number!\n";
	}
}
