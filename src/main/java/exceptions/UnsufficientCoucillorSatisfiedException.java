package exceptions;

public class UnsufficientCoucillorSatisfiedException extends Exception {
	public String showError() {
		return "Yout can't satisfy any Councillor! Re-insert correct Politic Cards!";
	}

}
