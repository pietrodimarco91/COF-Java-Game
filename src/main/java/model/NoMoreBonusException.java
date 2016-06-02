package model;

/**
 * This exception is invoked everytime a player satisfies the conditions to win
 * the specified bonus but they are no more available.
 * 
 * @author Riccardo
 *
 */
public class NoMoreBonusException extends Exception {
	private String typeOfBonus;

	public NoMoreBonusException(String typeOfBonus) {
		this.typeOfBonus = typeOfBonus;
	}

	public String showError() {
		return "You aren't able to win the " + typeOfBonus + ": these bonuses are no more available for this match!";
	}
}
