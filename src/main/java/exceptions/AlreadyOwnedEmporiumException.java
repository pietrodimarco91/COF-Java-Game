package exceptions;

public class AlreadyOwnedEmporiumException extends Exception {
	public String showError() {
		return "You already own an emporium in this city!";
	}
}
