package model;

public class ConfigObject {
	private int numberOfPlayers;
	private int bonusNumber;
	private int linksBetweenCities;
	
	public ConfigObject(int numberOfPlayers,int bonusNumber,int linksBetweenCities) {
		this.bonusNumber=bonusNumber;
		this.linksBetweenCities=linksBetweenCities;
		this.numberOfPlayers=numberOfPlayers;
	}
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	public int getBonusNumber() {
		return bonusNumber;
	}
	public int getLinksBetweenCities() {
		return linksBetweenCities;
	}
}
