package exceptions;

public class UnexistingConfigurationException extends Exception {
	public String printError() {
		return "The specified configuration doesn't exists, please type the correct number!\n";
	}
}
