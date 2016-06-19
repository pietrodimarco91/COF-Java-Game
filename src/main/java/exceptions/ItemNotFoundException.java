package exceptions;

public class ItemNotFoundException extends Exception {

	public String showError() {
		return "The specified item was not found in the Market!";
	}

}
