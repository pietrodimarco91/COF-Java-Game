package exceptions;

public class CardNotFoundException extends Exception {

	public String showError() {
		return "Card Not Found! You don't own a Politic Card of the specified color!";
	}
}
