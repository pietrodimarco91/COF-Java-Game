package model;

import java.io.Serializable;

public class ConfigObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer numberOfPlayers;
	private Integer nobilityTrackBonusNumber;
	private Integer linksBetweenCities;
	private Integer rewardTokenBonusNumber;
	private Integer permitTileBonusNumber;
	
	public ConfigObject(int id,int numberOfPlayers,int rewardTokenBonusNumber,int permitTileBonusNumber,int nobilityTrackBonusNumber,int linksBetweenCities) {
		this.id=new Integer(id);
		this.nobilityTrackBonusNumber=new Integer(nobilityTrackBonusNumber);
		this.permitTileBonusNumber=new Integer(permitTileBonusNumber);
		this.rewardTokenBonusNumber=new Integer(rewardTokenBonusNumber);
		this.linksBetweenCities=new Integer(linksBetweenCities);
		this.numberOfPlayers=new Integer(numberOfPlayers);
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
		String string="";
		string+="Board Configuration of 'Council Of Four' number "+id+"\n";
		string+="Number of players: "+this.numberOfPlayers+"\n";
		string+="Number of bonuses in Nobility Track cells: "+this.nobilityTrackBonusNumber+"\n";
		string+="Number of bonuses in Permit Tiles: "+this.permitTileBonusNumber+"\n";
		string+="Number of bonuses in Reward Tokens (City bonuses): "+this.rewardTokenBonusNumber+"\n";
		string+="Number of maximum outgoing connections from cities: "+this.linksBetweenCities+"\n";
		return string;
	}
	
}
