package model;

import java.io.Serializable;

/**
 * This class represent the objects that must be serialized to be written inside
 * the board.config binary file. It implements the Serializable interface and
 * all of the ConfigObjects are managed by the ConfigFileManager. A ConfigObject
 * represents exacly one board configuration, as it stores all the necessary
 * parameters for the game.
 * 
 * @author Riccardo
 *
 */
public class ConfigObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The ID of this configuration. Normally, the first configuration starts
	 * from 0 and this number increases. The first player has to choose the
	 * config ID.
	 */
	private Integer id;
	
	/**
	 * This attribute represents the MAXIMUM number of players for the match.
	 */
	private Integer numberOfPlayers;
	
	/**
	 * This attribute represents the number of bonuses inside the Nobility Track Cell that contain bonuses.
	 */
	private Integer nobilityTrackBonusNumber;
	
	/**
	 * This attribute represents the MAXIMUMX number of OUTGOING connections from a single City in the Board.
	 */
	private Integer linksBetweenCities;
	
	/**
	 * This attribute represents the number of bonuses inside each Reward Token (City bonus)
	 */
	private Integer rewardTokenBonusNumber;
	
	/**
	 * This attribute represents the number of bonuses inside each PermitTile
	 */
	private Integer permitTileBonusNumber;

	public ConfigObject(int id, int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
			int nobilityTrackBonusNumber, int linksBetweenCities) {
		this.id = new Integer(id);
		this.nobilityTrackBonusNumber = new Integer(nobilityTrackBonusNumber);
		this.permitTileBonusNumber = new Integer(permitTileBonusNumber);
		this.rewardTokenBonusNumber = new Integer(rewardTokenBonusNumber);
		this.linksBetweenCities = new Integer(linksBetweenCities);
		this.numberOfPlayers = new Integer(numberOfPlayers);
	}

	public int getId() {
		return this.id;
	}
	
	public int getNumberOfPlayers() {
		return this.numberOfPlayers.intValue();
	}

	public int getRewardTokenBonusNumber() {
		return this.rewardTokenBonusNumber.intValue();
	}

	public int getPermitTileBonusNumber() {
		return this.permitTileBonusNumber.intValue();
	}

	public int getNobilityTrackBonusNumber() {
		return this.nobilityTrackBonusNumber.intValue();
	}

	public int getLinksBetweenCities() {
		return linksBetweenCities.intValue();
	}

	public String toString() {
		String string = "";
		string += "Board Configuration of 'Council Of Four' number " + id + "\n";
		string += "Number of players: " + this.numberOfPlayers + "\n";
		string += "Number of bonuses in Nobility Track cells: " + this.nobilityTrackBonusNumber + "\n";
		string += "Number of bonuses in Permit Tiles: " + this.permitTileBonusNumber + "\n";
		string += "Number of bonuses in Reward Tokens (City bonuses): " + this.rewardTokenBonusNumber + "\n";
		string += "Number of maximum outgoing connections from cities: " + this.linksBetweenCities + "\n";
		return string;
	}

}
