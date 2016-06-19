package exceptions;

public class UnsufficientAssistantNumberException extends Exception {
	public String showError() {
		return "Unsifficient Number of Assistants! The specified Assistants aren't enough to perform this action";
	}
}
