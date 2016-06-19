package exceptions;

public class UnsufficientCouncillorsSatisfiedException extends Exception {
	public String showError() {
		return "Yout can't satisfy any Councillor! Re-insert correct Politic Cards!";
	}

}
