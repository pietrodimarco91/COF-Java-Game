package exceptions;

public class InvalidInputException extends Exception {
	public String printError() {
		return "ERROR: incorrect input. Please retry\n";
	}
}
