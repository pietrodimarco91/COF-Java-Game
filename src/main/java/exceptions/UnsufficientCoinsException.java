package exceptions;

public class UnsufficientCoinsException extends Exception {

	public String showError() {
		return "Unsifficient Coins! The specified coins aren't enough to perform this action";
	}
}
